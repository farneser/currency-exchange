<!DOCTYPE html>
<html>
<head>
    <title>Web Api</title>
</head>

<body style="display: flex; justify-content: space-around; flex-wrap: wrap;">

<div class="flex__item">

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
    </script>

    <h1>Currencies list</h1>

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

<div class="flex__item">

    <h1>Exchange rates list</h1>

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

<div class="flex__item">

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
        <br>
        <label>
            Currency code
            <input name="code" type="text" maxlength="3" minlength="0"/>
        </label>
        <br>
        <label>
            Currency sign
            <input name="sign" type="text"/>
        </label>
        <br>
        <input type="submit"/>
    </form>
</div>

<div class="flex__item">

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
        <br>
        <label>
            Target currency code
            <input name="targetCurrencyCode" type="text"/>
        </label>
        <br>
        <label>
            Exchange rate
            <input name="rate" type="number" step="0.000001"/>
        </label>
        <br>
        <input type="submit"/>
    </form>
</div>

<div class="flex__item">

    <iframe name="conversionFrame" id="conversionFrame" style="display: none;"></iframe>

    <script>
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

    </script>

    <form action="exchange" target="conversionFrame" method="get">
        <h1>Exchange Currencies</h1>

        <label>
            Base currency code
            <input name="from" type="text"/>
        </label>
        <br>
        <label>
            Target currency code
            <input name="to" type="text"/>
        </label>
        <br>
        <label>
            Amount
            <input name="amount" type="number" step="0.000001"/>
        </label>
        <br>
        <input type="submit"/>
    </form>
    <div id="converted">Converted:</div>
</div>

<style>
    .flex__item {
        margin-left: 1%;
        margin-right: 1%;
    }
</style>

</body>
</html>
