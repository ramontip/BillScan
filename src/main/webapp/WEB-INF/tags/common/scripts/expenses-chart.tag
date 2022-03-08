<script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/3.6.0/chart.min.js"
        integrity="sha512-GMGzUEevhWh8Tc/njS0bDpwgxdCJLQBWG3Z2Ct+JGOpVnEmjvNx6ts4v6A2XJf1HOrtOsfhv3hBKpK9kE5z8AQ=="
        crossorigin="anonymous" referrerpolicy="no-referrer"></script>

<script>
    const ctx = document.getElementById('expensesChart').getContext('2d');

    // Get Prices
    const price = ${data};

    // Get Store Names
    const labels = ${labels};

    const length = labels.length;

    const Color = [];


    function getRandomColor() {
        const firstRndInt = Math.floor(Math.random() * 255) + 1;
        const secondRndInt = Math.floor(Math.random() * 255) + 1;
        const thirdRndInt = Math.floor(Math.random() * 255) + 1;
        const color = "rgb(" + firstRndInt + "," + secondRndInt + "," + thirdRndInt + ")";
        return color;
    }

    for (let i = 0; i < length; i++) {
        Color.push(getRandomColor());
    }

    //console.log(Color);

    //Create Data Structure
    const data = {
        labels: labels,
        datasets: [{
            label: '# of Votes',
            data: price,
            backgroundColor: Color,
            borderWidth: 1
        }]
    };

    //Create Options
    const options = {
        maintainAspectRatio: false,
        plugins: {
            legend: {
                position: 'top',
            },
        }
    };

    // Chart config
    const config = {
        type: 'doughnut',
        data: data,
        options: options
    };

    const expensesChart = new Chart(ctx, config);


    //console.log('${labels}');
</script>