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

                    console.log(exchangeRate)

                    exchangeRates.append(getExchangeRateBlock(exchangeRate));

                }
            })
    </script>
</div>

<div class="addCurrency"></div>
<div class="addExchangeRate"></div>

<div class="conversion">conversion</div>
</body>
</html>
