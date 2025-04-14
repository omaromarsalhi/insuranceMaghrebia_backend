from llama_index.llms.gemini import Gemini

from fastApi.data_agent.Config import Config


def semantic_comparison(previous_user_query: str, user_query: str, config: Config) -> bool:
    model = Gemini(config.get('API', 'gemini_key'))
    result = str(model.complete("perform a semantic comparison between the following two sentences: "
                                + previous_user_query + " and " + user_query
                                + " no matter what JUST answer me with yes or no only"))
    return result.lower() == "yes"