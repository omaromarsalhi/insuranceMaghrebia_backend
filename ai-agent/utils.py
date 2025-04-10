import re



def clean_json_response(response):
    # Replace \s with \\s only if \\s is not already in the regex field
    cleaned_response = re.sub(r'(?<!\\)\\s', r'\\\\s', response)
    return cleaned_response
