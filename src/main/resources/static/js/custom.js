var selectPeriod = document.getElementById("selectPeriod");
if (selectPeriod) {
    document.getElementById("selectPeriod").addEventListener("change", () => {
        document.getElementById("filterForm").submit();
    });
}

var selectSort = document.getElementById("selectSort");
if (selectSort) {
    document.getElementById("selectSort").addEventListener("change", () => {
        document.getElementById("filterForm").submit();
    });
}