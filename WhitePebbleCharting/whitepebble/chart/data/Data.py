'''
Created on Jul 17, 2014

@author: Harish
'''



import pandas.io.data as web 
import datetime
from pandas import Series
import plotly.plotly as py
from plotly.graph_objs import *
#DEFINE current list of tickers in a global list variable, change this later
transactions_loc = r"C:\Users\Harish\workspace\WhitePebbleCharting\whitepebble\resources\transactions.csv" #Make this relative later
fromDate = datetime.date(2014, 7 , 11)
sector_maps = {
               "Consumer Cyclical":["AMZN", "ATVI", "DLPH", "DDS", "DNKN", "FL", 
                                    "F", "GPS", "GM", "GNTX","HD", "ORLY", "WSM"],
               "Consumer Non-Cyclical":["KR", "SWY"],
               "Telecommunications":["T", "TMUS", "VZ", ],
               "Financials":["DFS", "V", "WFC", "TMK", ]
               }
def get_data(tickerList, fromDate, toDate):
    data = web.get_data_google(tickerList, fromDate, toDate, ret_index=True)
    return data
    
def get_priceondate(ticker, date, data): #returns closing price of ticker on date
    return data["Close"][ticker][date]

def get_holdings_value(data, holdings, date):
    nav=0.0
    for ticker in holdings.keys():
        val = get_priceondate(ticker, date, data) * holdings[ticker]
        nav += val
    return nav

def get_benchmark_series(benchmark):
    return web.get_data_yahoo([benchmark], fromDate)["Close"][benchmark] 

def get_portfolio_series(data, transactions):
    navs = []
    curr_cash = 1000000.0
    curr_holdings = {} #Key:Ticker, Amt
    dates = sorted(data.major_axis)
    for date in dates:
        datecopy = datetime.date(date.year, date.month, date.day)
        if transactions.has_key(datecopy):#convert date to a datetime.date
            for transaction in transactions[datecopy]: #[[Type, Ticker, Amt. Shares]]
                type = transaction[0]
                ticker = transaction[1]
                amt = transaction[2]
                price = get_priceondate(ticker, date, data)
                if (type == "BUY"):
                    if curr_holdings.has_key(ticker):
                        curr_cash  = curr_cash - (amt * price)
                        curr_holdings[ticker] += amt
                    else:
                        curr_cash  = curr_cash - (amt * price)
                        curr_holdings[ticker] = amt
                    assert (curr_cash >= 0.0)
                elif (type == "SELL"):
                    assert curr_holdings.has_key(ticker)
                    curr_holdings[ticker] =  curr_holdings[ticker] - amt
                    assert (curr_holdings[ticker] >= 0.0) #No short selling allowed
                    curr_cash  = curr_cash + (amt * price)
                else:
                    raise Exception("Unknown order type:"+ type)
                    
                    
        #calculate NAV for that day now that transactions are completed
        asset_value = get_holdings_value(data, curr_holdings, date)
        nav = asset_value + curr_cash
        navs.append(nav)
    return Series(data=navs,index=dates), get_sector_weightage(curr_holdings, data)

def get_sector_weightage(holdings, data):
    weightage = {}
    for sector in sector_maps.keys():
        for ticker in sector_maps[sector]:
            val = get_priceondate(ticker, max(data.major_axis), data) * holdings[ticker]
            if weightage.has_key(sector):
                weightage[sector] += val
            else:
                weightage[sector] = val
    nav = sum(weightage.values())
    for sector in weightage:
        weightage[sector] = (weightage[sector] / nav) * 100
    return weightage

def plot_sectorchart(weightage):
    py.sign_in("Harish92", "s47zjh2bv8")
    x = []
    y = []
    for sector in weightage.keys():
        x.append(sector)
        y.append(weightage[sector])
    data = Data([Bar(x=x, y=y, name="WhitePebble")])
    layout = Layout(
                    title="Sector Weightage",
                    xaxis=XAxis(
                                title="Sector",
                                titlefont={'color': '#000000', 'family': 'Courier New, monospace', 'size': 20}
                    ),
                    yaxis=YAxis(
                                title='Weightage(%)',
                                titlefont={'color': '#000000', 'family': 'Courier New, monospace', 'size': 20}

                    )
                    )
    fig = Figure(data=data, layout=layout)
    plot_url = py.plot(fig, filename="sector_weight")
    
                
def plot_chart(portfolio_series,benchmark_series):
    py.sign_in("Harish92", "s47zjh2bv8")
    fund_plot = Scatter(x=portfolio_series.index, y=portfolio_series.values, name="WhitePebble Consumer Discretionary")
    benchmark_plot = Scatter(x=benchmark_series.index, y=benchmark_series.values, name="Russell 1000")
    data = Data([fund_plot, benchmark_plot])    
    layout = Layout(
    title='WhitePebble ETF Performance',
    xaxis=XAxis(
        title='Date',
        titlefont={'color': '#000000', 'family': 'Courier New, monospace', 'size': 20}
    ),
    yaxis=YAxis(
        title='Perf (%)',
        titlefont={'color': '#000000', 'family': 'Courier New, monospace', 'size': 20}
    )   
    )
    
    fig = Figure(data=data, layout=layout)
    plot_url = py.plot(fig, filename="etf_perf")
    
def get_returnindex(series):
    startVal = series[fromDate]
    return (series / startVal) - 1
            
if __name__ == '__main__':
    benchmark ="RUI"
    tickerList = []
    transactions = {} #Key:Date, Value:[[Type, Ticker, Amt. Shares], [Type, Ticker, Amt. Shares], ...]
    count = 0
    for line in open(transactions_loc):
        if (count != 0):
            line = line.replace("\n", "")
            [ticker, type, date, numshares, weight] = line.split(",")
            [month, day, year] = [int(x) for x in date.split("/")]
            date = datetime.date(year, month, day)
            if (not transactions.has_key(date)):
                transactions[date] = []
            transactions[date].append([type, ticker, float(numshares)])
            tickerList.append(ticker)
        else:
            pass
        count += 1
        
    data = get_data(tickerList, fromDate, None)
    print "Finished getting data..."
    portfolio_series, weightage = get_portfolio_series(data, transactions)
    benchmark_series = get_benchmark_series(benchmark)
    portfolio_returnindex = get_returnindex(portfolio_series)
    benchmark_returnindex = get_returnindex(benchmark_series)
    print weightage
    plot_sectorchart(weightage)
    # plot_chart(portfolio_returnindex,benchmark_returnindex)
    


    
        
        
        
        
        