<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Currency Exchanger</title>
</head>
<body>
<header>
    <h1>Currency Exchange</h1>
</header>

<div class="container">
    <div class="flex__item">
        <h1>Currencies list</h1>
        <table id="currencies">
            <tr>
                <th>Code</th>
                <th>Name</th>
                <th>Sign</th>
            </tr>
        </table>
    </div>

    <div class="flex__item">
        <h1>Exchange rates list</h1>
        <table id="exchangeRates">
            <tr>
                <th>Currency</th>
                <th>Rate</th>
                <th>Edit</th>
            </tr>
        </table>
    </div>

    <div class="flex__item">
        <h1>Create Currency</h1>
        <form action="currencies" target="currenciesFrame" method="post">
            <label>
                Currency name
                <input name="name" type="text" required/>
            </label>
            <br>
            <label>
                Currency code
                <input name="code" type="text" maxlength="3" minlength="0" required/>
            </label>
            <br>
            <label>
                Currency sign
                <input name="sign" type="text" required/>
            </label>
            <br>
            <input type="submit" value="Create"/>
        </form>
    </div>

    <div class="flex__item">
        <h1>Create Exchange Rate</h1>
        <form action="exchangeRates" target="exchangeRatesFrame" method="post">
            <label>
                Base currency code
                <input name="baseCurrencyCode" type="text" required/>
            </label>
            <br>
            <label>
                Target currency code
                <input name="targetCurrencyCode" type="text" required/>
            </label>
            <br>
            <label>
                Exchange rate
                <input name="rate" type="number" step="0.000001" required/>
            </label>
            <br>
            <input type="submit" value="Create"/>
        </form>
    </div>

    <div class="flex__item">
        <h1>Exchange Currencies</h1>
        <form action="exchange" target="conversionFrame" method="get">
            <label>
                Base currency code
                <input name="from" type="text" required/>
            </label>
            <br>
            <label>
                Target currency code
                <input name="to" type="text" required/>
            </label>
            <br>
            <label>
                Amount
                <input name="amount" type="number" step="0.000001" required/>
            </label>
            <br>
            <input type="submit" value="Exchange"/>
        </form>
        <div id="converted">Converted:</div>
    </div>
</div>

<iframe name="currenciesFrame" id="currenciesFrame" style="display: none;"></iframe>
<iframe name="exchangeRatesFrame" id="exchangeRatesFrame" style="display: none;"></iframe>
<iframe name="conversionFrame" id="conversionFrame" style="display: none;"></iframe>

<script>
    const getCurrencyBlock = (currency) => {
        const line = document.createElement("tr")

        const code = document.createElement("td");
        code.innerText = currency["code"]
        line.append(code);

        const name = document.createElement("td");
        name.innerText = currency["name"]
        line.append(name);

        const sign = document.createElement("td");
        sign.innerText = currency["sign"]
        line.append(sign);

        return line;
    }

    const getExchangeRateBlock = (exchangeRate) => {
        const line = document.createElement("tr")

        const currenciesCode = exchangeRate.baseCurrency.code + exchangeRate.targetCurrency.code;

        const code = document.createElement("td");
        code.innerText = currenciesCode;
        line.append(code);

        const rate = document.createElement("td");
        rate.innerText = exchangeRate["rate"];
        rate.classList = "rate";
        line.append(rate);

        const edit = document.createElement("button");
        edit.innerHTML = "edit"
        line.append(edit);
        edit.id = "edit_" + currenciesCode;
        edit.addEventListener("click", editRate);

        line.id = "exchangeRate_" + currenciesCode;
        return line;
    }

    const editRate = (event) => {
        const id = event.target.id.substring(5);

        const rate = prompt("Enter new rate:");

        if (rate == null || rate === "" || parseFloat(rate) === 0) {
            alert("Please enter double value")
            return;
        }

        const details = {
            'rate': rate
        };

        let formBody = [];

        for (const property in details) {
            const encodedKey = encodeURIComponent(property);
            const encodedValue = encodeURIComponent(details[property]);
            formBody.push(encodedKey + "=" + encodedValue);
        }

        formBody = formBody.join("&");

        fetch(window.location.pathname + "exchangeRate/" + id,
            {
                method: "PATCH",
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8',
                },
                body: formBody,
            })
            .then(d => d.json())
            .then(r => {
                const exchangeRate = document.getElementById("exchangeRate_" + id).getElementsByClassName("rate")[0];
                exchangeRate.innerText = r.rate;
            });
    }

    const currenciesFrame = document.getElementById("currenciesFrame");

    currenciesFrame.addEventListener("load", () => {
        const response = JSON.parse(currenciesFrame.contentDocument.body.innerText);

        const keys = Object.keys(response);

        if (keys.length === 1 && keys[0] === "message") {
            alert(response.message)
            return;
        }

        const currencies = document.getElementById("currencies");
        currencies.append(getCurrencyBlock(response))
    })

    const exchangeRatesFrame = document.getElementById("exchangeRatesFrame");

    exchangeRatesFrame.addEventListener("load", () => {
        const response = JSON.parse(exchangeRatesFrame.contentDocument.body.innerText);

        const keys = Object.keys(response);

        if (keys.length === 1 && keys[0] === "message") {
            alert(response.message)
            return;
        }

        const exchangeRates = document.getElementById("exchangeRates");
        exchangeRates.append(getExchangeRateBlock(response))
    })

    const conversionFrame = document.getElementById("conversionFrame");

    conversionFrame.addEventListener("load", () => {
        const response = JSON.parse(conversionFrame.contentDocument.body.innerText);

        const keys = Object.keys(response);

        const converted = document.getElementById("converted");

        if (keys.length === 1 && keys[0] === "message") {
            alert(response.message)
            converted.innerText = "Converted: "
            return;
        }

        converted.innerText = "Converted: " + response.amount + " " + response.baseCurrency.code + " to " + response.targetCurrency.code + " - " + response.convertedAmount;
    })

    const currencies = document.getElementById("currencies")

    fetch(window.location.pathname + "currencies")
        .then(response => response.json())
        .then(data => {
            for (let id in data) {
                const currency = data[id];

                currencies.append(getCurrencyBlock(currency));
            }
        })

    const exchangeRates = document.getElementById("exchangeRates")

    fetch(window.location.pathname + "exchangeRates")
        .then(response => response.json())
        .then(data => {
            for (let id in data) {
                const exchangeRate = data[id];

                exchangeRates.append(getExchangeRateBlock(exchangeRate));

            }
        })
</script>

<style>

    body, h1, h2, h3, p, ul, li, form, table {
        margin: 0;
        padding: 0;
    }

    body {
        background-color: #f9f9f9;
        color: #333;
        font-family: 'Arial', sans-serif;
        line-height: 1.6;
    }

    header {
        background-color: #333;
        color: #fff;
        padding: 20px 0;
        text-align: center;
        font-size: 24px;
    }

    .container {
        display: flex;
        flex-wrap: wrap;
        justify-content: space-around;
        margin: 20px;
    }

    #exchangeRates tr button {
        width: 90%;
        margin-top: 2px;
        margin-left: 2px;
    }

    .flex__item {
        width: 300px;
        margin-bottom: 20px;
        padding: 2%;
        background-color: #fff;
        border: 1px solid #ddd;
        border-radius: 5px;
        box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    }

    table {
        width: 100%;
        border-collapse: collapse;
    }

    table, th, td {
        border: 1px solid #ddd;
    }

    th, td {
        padding: 12px;
        text-align: left;
    }

    form {
        margin-top: 10px;
    }

    label {
        display: block;
        margin-bottom: 8px;
        font-weight: bold;
    }

    input[type="text"],
    input[type="number"] {
        width: 92%;
        padding: 10px;
        border: 1px solid #ddd;
        border-radius: 3px;
        font-size: 16px;
    }

    input[type="submit"], button {
        background-color: #333;
        color: #fff;
        border: none;
        padding: 10px 20px;
        border-radius: 3px;
        cursor: pointer;
        font-size: 16px;
        transition: background-color 0.3s ease;
    }

    input[type="submit"]:hover {
        background-color: #555;
    }

    iframe {
        width: 100%;
        height: 300px;
        border: none;
    }

</style>

</body>
</html>
