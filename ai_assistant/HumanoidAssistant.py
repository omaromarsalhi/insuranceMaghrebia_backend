import asyncio
import json
import google.generativeai as genai
from datetime import datetime
from typing import Dict, Tuple, Callable, Awaitable
import requests
import re

class HumanoidAssistant:
    def __init__(self):
        self.chat_history = []
        self.model = genai.GenerativeModel('gemini-1.5-pro')
        genai.configure(api_key="AIzaSyCA-aljPzSZbfkkyB1zYRqZfcfdXGhT6qc")

        self.functions = {
        
            "send_email": {
        "description": "Envoyer un e-mail à un destinataire",
        "params": {
            "recipient": {"description": "Adresse e-mail du destinataire", "type": "email"},
            "subject": {"description": "Sujet du message", "type": "str"},
            "message": {"description": "Contenu du message", "type": "str"}
        },
        "endpoint": "send",
    },
            "redirect_to_page": {
                "description": "Rediriger l'utilisateur vers une autre page",
                "params": {
                    "page": {"description": "Nom de la page ou URL de destination", "type": "str"}
                },
                "endpoint": "redirect",
            }
            
        }

        self.backend_config = {
            "base_url": "http://localhost:8091/api/v1/report",
            "headers": {"Content-Type": "application/json"}
        }
    
    def _extract_action(self, user_input: str) -> Dict:
        """Extrait l'intention et les paramètres avec le nouveau format"""
        functions_desc = "\n".join(
            f"- {name}: {details['description']}\n  Paramètres: {list(details['params'].keys())}"
            for name, details in self.functions.items()
        )
        
        prompt = f"""
        Analyse cette commande utilisateur et génère un JSON valide avec:
        - intent (parmi {list(self.functions.keys())})
        - parameters (selon l'intent)
        - required_parameters : les paramètres requis pour cette intention
        - missing_parameters : ceux requis mais absents ou vides

        Format de sortie :
        {{
            "intent": "string",
            "parameters": {{
                "key1": "value1",
                "key2": "value2"
            }},
            "required_parameters": ["key1", "key2"],
            "missing_parameters": []
            
        }}

        Fonctions disponibles :
        {functions_desc}

        Exemple :
        Commande: "Envoie un email à test@example.com avec le sujet 'Important'"
        Sortie:
        {{
            "intent": "send_email",
            "parameters": {{
                "recipient": "test@example.com",
                "subject": "Important",
                "message": ""
            }},
            "required_parameters": ["recipient", "subject", "message"],
            "missing_parameters": ["message"]
        }}

        Commande : "{user_input}"
        """
        
        try:
            response = self.model.generate_content(prompt)
            cleaned = response.text.replace("```json", "").replace("```", "").strip()
            action_data = json.loads(cleaned)
            print(action_data)
            
            if "intent" not in action_data or action_data["intent"] not in self.functions:
                return {"error": "Intention non reconnue"}
                
            return action_data
        except Exception as e:
            return {"error": str(e)}
    async def _validate_params_async(
        self,
        action_data: Dict,
        input_handler: Callable[[str], Awaitable[str]]
    ) -> Dict:
        """
        Valide les paramètres, vérifie les types et récupère les informations manquantes via input_handler.
        """
        if "error" in action_data:
            return action_data

        intent = action_data["intent"]
        params = action_data.get("parameters", {})
        results = {}

        # Étape 1: Validation des paramètres existants
        invalid_params = {}
        for param, param_info in self.functions[intent]["params"].items():
            param_type = param_info["type"]
            value = params.get(param)
            if value and not self._validate_type(value, param_type):
                print(f"[WARN] Paramètre '{param}' présent mais invalide (attendu: {param_type})")
                invalid_params[param] = param_info

        # Étape 2: Détermination des paramètres manquants ou invalides
        required_params = self.functions[intent]["params"].keys()
        missing = [
            param for param in required_params
            if not str(params.get(param, "")).strip() or param in invalid_params
        ]

        # Étape 3: Demander à l'utilisateur les valeurs manquantes ou invalides
        for param in missing:
            param_info = self.functions[intent]["params"][param]
            desc = param_info["description"]
            param_type = param_info["type"]
            while True:
                try:
                    value = await input_handler(desc)
                    try:
                        parsed = json.loads(value)
                        if isinstance(parsed, dict) and "value" in parsed:
                            value = parsed["value"]
                    except Exception:
                        pass

                    if not self._validate_type(value, param_type):
                        raise ValueError(f"Le paramètre '{param}' doit être de type {param_type}.")

                    if param_type == "date" and not self._validate_date(value):
                        raise ValueError("Format AAAA-MM-JJ requis pour la date.")

                    results[param] = value
                    break
                except ValueError as e:
                    print(f"{str(e)} - Réessayez...")
                    await input_handler(f"{str(e)} Pourriez-vous réessayer avec une valeur correcte ?")

        # Fusion des anciens et nouveaux paramètres (avec écrasement des invalides corrigés)
        return {
            "intent": intent,
            "parameters": {**params, **results},
            "missing": missing
        }

    # async def _validate_params_async(
    #     self,
    #     action_data: Dict,
    #     input_handler: Callable[[str], Awaitable[str]]
    # ) -> Dict:
    #     """
    # Valide les paramètres, vérifie les types et récupère les informations manquantes via input_handler.
    # """
    #     if "error" in action_data:
    #         return action_data

    #     intent = action_data["intent"]
    #     params = action_data.get("parameters", {})
    #     # print(params)
    #     required_params = self.functions[intent]["params"].keys()
    #     missing = [param for param in required_params if not str(params.get(param, "")).strip()]
    #     results = {}

    #     for param in missing:
    #         param_info= self.functions[intent]["params"][param]
            
    #         desc = param_info["description"]
    #         param_type = param_info["type"]
    #         print(param_type)
    #         while True:
    #             try:
    #                 value = await input_handler(desc)
    #                 try:
    #                     parsed = json.loads(value)
    #                     if isinstance(parsed, dict) and "value" in parsed:
    #                         value = parsed["value"]
    #                 except Exception:
    #                     pass    

    #                 if not self._validate_type(value, param_type):
    #                     raise ValueError(f"Le paramètre '{param}' doit être de type {param_type}.")
                
    #                 if param_type == "date" and not self._validate_date(value):
    #                     raise ValueError("Format AAAA-MM-JJ requis pour la date.")
                    
    #                 results[param] = value
    #                 break
    #             except ValueError as e:
    #                 print(f" {str(e)} - Réessayez...")
    #                 await input_handler(str(e))
        
       
    #     return {
    #         "intent": intent,
    #         "parameters": {**params, **results},
    #         "missing": missing
    #     }
    

    def _validate_type(self, value: str, expected_type: str) -> bool:
        """
        Valide le type de la valeur selon l'attendu (par exemple, email, str, int).
        """
        if expected_type == "str":
            return isinstance(value, str)
        elif expected_type == "email":
            print(value)
            return bool(re.match(r"[^@]+@[^@]+\.[^@]+", value))
        elif expected_type == "int":
            return value.isdigit()
        elif expected_type == "date":
            return bool(re.match(r"\d{4}-\d{2}-\d{2}", value))
       
        return True

    def _validate_date(self, date_str: str) -> bool:
        try:
            datetime.strptime(date_str, "%Y-%m-%d")
            return True
        except ValueError:
            return False

    def _execute_action(self, intent: str, params: Dict) -> Tuple[str, bool]:
        try:
            if intent == "redirect_to_page":
        
                return json.dumps({
                    "type": "redirect",
                    "target": params["page"]
                }), True
            endpoint = self.functions[intent]["endpoint"]
            result, status_code = self._call_backend(endpoint, params)
        
            if status_code == 200:
                if intent == "stock_price":
                    return f" Cours de {params['ticker']} : {result.get('price', 'N/A')}$", True
                elif intent == "send_email":
                    return f" E-mail envoyé à {params['recipient']} !", True
            return f" Erreur {status_code} : {result.get('error', '')}", False
            
        except Exception as e:
            return f"⚠️ Erreur critique : {str(e)}", False
    
    def _get_invalid_parameters(self, intent: str, parameters: Dict) -> Dict[str, str]:
        invalid = {}
        for param, value in parameters.items():
            expected_type = self.functions[intent]["params"][param]["type"]
            if not self._validate_type(value, expected_type):
                invalid[param] = expected_type
        return invalid
    
    def _call_backend(self, endpoint: str, data: Dict) -> Tuple[Dict, int]:
        try:
            response = requests.post(
                f"{self.backend_config['base_url']}/{endpoint}",
                headers=self.backend_config['headers'],
                 json=data 
            )
            print(data)
            response.raise_for_status()
            return response.json(), response.status_code
        except requests.exceptions.RequestException as e:
            return {"error": str(e)}, getattr(e.response, 'status_code', 500)
        except json.JSONDecodeError:
            return {"error": "Réponse invalide du serveur"}, 500

