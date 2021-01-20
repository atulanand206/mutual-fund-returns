# Mutual Fund Returns

Historical NAV data for Indian mutual funds are provided by the site https://www.mfapi.in/.

For instance NAV data of “SBI Equity Hybrid Fund” can be got in JSON format using the URL
https://api.mfapi.in/mf/102885

#### Problem Statement
You are required to compute the trailing returns for a Mutual scheme investment month wise
for different time period of investments over a time period (horizon).

Returns are calculated using the formula.

**Returns = (End Value/Start Value)^(1/Years)-1**

Say a mutual scheme started with a value of Rs 1000 on January 1, 2019, ended with NAV of Rs
2500 on January 1, 2026.
Then year-on-year return will be calculated like this
Returns = (2500/1000)^(1/7) – 1 = 0.14 =~ 14%

You are required to find the trailing returns for given period of investment and horizon.
Horizon means for how many months you need to find the returns.
Program outline
1. Get the scheme number from user
2. Download historic NAV data from mfapi site
3. Get the period of investment and horizon from the user
4. Compute trailing returns for entire horizon month over month for the latest date
available in NAV data

##### Example
You are running the program on 16-Jul-20, the latest NAV you will have would be for 15-Jul-
20

Input

Period of Investment = 1

Horizon = 1

Output

Month wise returns

|  Month | Returns |      Calculation     |
|--------|---------|----------------------|
| Aug-19 |  xx%    |Start nav – 16-Aug-18 |
|        |         |End nav – 16-Aug-19   |
|        |         |                      |  
| Sep-19 |  xx%    |Start nav – 16-Sep-18 |
|        |         |End nav – 16-Sep-19   |
|        |         |                      |  
| Oct-19 |  xx%    |Start nav – 16-Oct-18 |
|        |         |End nav – 16-Oct-19   |
|        |         |                      |  
| Nov-19 |  xx%    |Start nav – 16-Nov-18 |
|        |         |End nav – 16-Nov-19   |
|        |         |                      |
|        |         |                      |
|        |         |                      |
|        |         |                      |
|        |         |                      |
|        |         |                      |
| Jul-20 |  xx%    |Start nav – 16-Jul-19 |
|        |         |End nav – 16-Jul-20   |


##### Note
1. If NAV for a particular date is not available, choose the NAV of next available date
2. If data is not available (if date is before launch date) return zero as the return

##### Tech stack
1. Any tech stack comfortable
2. App can be a pure command line based application built on Java or Spring or Python or it can
be pure JavaScript app built on top of framework like Angular or react

##### Points evaluated
1. Code organization &amp; structuring
2. Testability of code
3. Either actual tests or at least clear ideas of what/how to test

#### Solution
The solution is implemented to provide the following web controller endpoints.
1. Get Schemes: `/funds`
2. Get Scheme by code: `/funds/{fundCode}?period={period}&horizon={horizon}` 

#### Reference
1. https://www.investopedia.com/ask/answers/050415/what-good-annual-return-mutual-scheme.asp
2. https://www.investopedia.com/articles/08/annualized-returns.asp
