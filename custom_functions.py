import random

def generatePrivateUniqueId(length, adder=''):
    characters = 'qwertyuiopasdfghjklzxcvbnm' + '1234567890'
    unique_id = ''.join(random.choices(characters, k=length))
    if len(adder) > 0:
        return adder+"-"+unique_id
    else:
        return unique_id