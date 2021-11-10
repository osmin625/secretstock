import pandas as pd
import firebase_admin
from firebase_admin import credentials
from firebase_admin import db

def getStockCode(market):
    if market == 'kosdaq':
        url_market = 'kosdaqMkt'
    elif market == 'kospi':
        url_market = 'stockMkt'
    else:
        print('invalid market ')
        return
    url = 'http://kind.krx.co.kr/corpgeneral/corpList.do?method=download&searchType=13&marketType=%s' % url_market
    df = pd.read_html(url, header=0)[0]
    return df

db_url = 'https://teamproject-da28a-default-rtdb.firebaseio.com/'
cred = credentials.Certificate('myStockKey.json')
firebase_admin.initialize_app(cred,{
    'databaseURL' : db_url
})
if __name__ == '__main__':
    result_df = getStockCode('kosdaq')
    a = result_df[['종목코드','회사명']]
    for i in range(0,len(a)):
         company_name = str(a['회사명'][i])
         company_code = int(a['종목코드'][i])
         ref = db.reference('stock')
         ref.update({company_code : company_name})
    result_kp = getStockCode('kospi')
    b = result_kp[['종목코드','회사명']]
    for i in range(0,len(b)):
         company_name = str(b['회사명'][i])
         company_code = int(b['종목코드'][i])
         ref = db.reference('stock')
         ref.update({company_code : company_name})

