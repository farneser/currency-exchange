<!DOCTYPE html>
<html>
<head>
    <title>Web Api</title>
</head>
<body>

<div class="currencies__list">

    <script>
        const getCurrencyBlock = (currency) => {
            let line = document.createElement("tr")

            let code = document.createElement("td");
            code.innerText = currency["code"]
            line.append(code);

            let name = document.createElement("td");
            name.innerText = currency["name"]
            line.append(name);

            let sign = document.createElement("td");
            sign.innerText = currency["sign"]
            line.append(sign);

            return line;
        }

        const getExchangeRateBlock = (exchangeRate) => {
            let line = document.createElement("tr")

            let code = document.createElement("td");
            code.innerText = exchangeRate["baseCurrency"]["code"] + exchangeRate["targetCurrency"]["code"];
            line.append(code);

            let rate = document.createElement("td");
            rate.innerText = exchangeRate["rate"];
            line.append(rate);

            let edit = document.createElement("button");
            edit.innerHTML = "edit"
            line.append(edit);

            return line;
        }
    </script>


    <table id="currencies">
        <tr>
            <th>Code</th>
            <th>Name</th>
            <th>Sign</th>
        </tr>
    </table>

    <script>
        const currencies = document.getElementById("currencies")

        fetch(window.location.pathname + "currencies")
            .then(response => response.json())
            .then(data => {
                for (let id in data) {
                    const currency = data[id];

                    currencies.append(getCurrencyBlock(currency));
                }
            })
    </script>
</div>

<div class="exchangeRates__list">
    <table id="exchangeRates">
        <tr>
            <th>Currency</th>
            <th>rate</th>
            <th>Edit</th>
        </tr>
    </table>

    <script>

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
</div>

<div class="addCurrency">

    <iframe name="currenciesFrame" id="currenciesFrame" style="display: none;"></iframe>

    <script>
        const currenciesFrame = document.getElementById("currenciesFrame");

        currenciesFrame.addEventListener("load", () => {
            const response = JSON.parse(currenciesFrame.contentDocument.body.innerText);

            const keys = Object.keys(response);

            if (keys.length === 1 && keys[0] === "message") {
                alert(response.message)
                return;
            }

            currencies.append(getCurrencyBlock(response))

        })

    </script>

    <form action="currencies" target="currenciesFrame" method="post">
        <h1>Create Currency</h1>

        <label>
            Currency name
            <input name="name" type="text"/>
        </label>
        <label>
            Currency code
            <input name="code" type="text" maxlength="3" minlength="0"/>
        </label>

        <label>
            Currency sign
            <input name="sign" type="text"/>
        </label>

        <input type="submit"/>
    </form>
</div>
<div class="addExchangeRate">
    <iframe name="exchangeRatesFrame" id="exchangeRatesFrame" style="display: none;"></iframe>

    <script>
        const exchangeRatesFrame = document.getElementById("exchangeRatesFrame");

        exchangeRatesFrame.addEventListener("load", () => {
            const response = JSON.parse(exchangeRatesFrame.contentDocument.body.innerText);

            const keys = Object.keys(response);

            if (keys.length === 1 && keys[0] === "message") {
                alert(response.message)
                return;
            }

            exchangeRates.append(getExchangeRateBlock(response))

        })

    </script>

    <form action="exchangeRates" target="exchangeRatesFrame" method="post">
        <h1>Create Exchange Rate</h1>

        <label>
            Base currency code
            <input name="baseCurrencyCode" type="text"/>
        </label>
        <label>
            Target currency code
            <input name="targetCurrencyCode" type="text"/>
        </label>
        <label>
            Exchange rate
            <input name="rate" type="text" step="0.000001"/>
        </label>

        <input type="submit"/>
    </form>
</div>

<div class="conversion">conversion</div>
</body>
</html>
