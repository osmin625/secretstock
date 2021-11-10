import firebase_admin
from firebase_admin import credentials
from firebase_admin import db
from bs4 import BeautifulSoup
from datetime import datetime
import requests
import time

def get_code(company_code):
    url = 'https://finance.naver.com/item/main.naver?code=' + company_code
    result = requests.get(url)
    bs_obj = BeautifulSoup(result.content, "html.parser")
    return bs_obj

def get_price(company_code):
    bs_obj = get_code(company_code)
    no_today = bs_obj.find("p", {"class" : "no_today"})
    blind = no_today.find("span", {"class" : "blind"})
    now_price = blind.text
    return now_price
# def get_name(company_code):
#     bs_obj = get_code(company_code)
#     com_name = bs_obj.find("h2", {"class" : "wrap_company"})
#     company_name = com_name.text
#     return company_name

company_code = ["141080","005930","051910","207940"]

db_url = 'https://hello-86289-default-rtdb.firebaseio.com/'
cred = credentials.Certificate('myKey.json')
firebase_admin.initialize_app(cred,{
    'databaseURL' : db_url
})

while(1):
    now = datetime.now()
    print(now)
    for item in company_code:
        now_price = get_price(item)
        # name = get_name(item)
        print(item +' : ' + now_price)
        ref = db.reference()
        ref.update({item : now_price})
    time.sleep(60)
