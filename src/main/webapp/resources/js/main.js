Chart.defaults.global.pointHitDetectionRadius = 1;
Chart.defaults.global.tooltips.enabled = false;
Chart.defaults.global.tooltips.mode = 'index';
Chart.defaults.global.tooltips.position = 'nearest';
Chart.defaults.global.tooltips.custom = CustomTooltips;
var cardChart1 = new Chart($('#card-chart1'), {
    type: 'line',
    data: {
        labels: ['January', 'February', 'March', 'April', 'May', 'June', 'July'],
        datasets: [{
                backgroundColor: getStyle('--primary'),
                borderColor: 'rgba(255,255,255,.55)',
                data: [65, 59, 84, 84, 51, 55, 40]
            }]
    },
    options: {
        maintainAspectRatio: false,
        legend: {
            display: false
        },
        scales: {
            xAxes: [{
                    gridLines: {
                        color: 'transparent',
                        zeroLineColor: 'transparent'
                    },
                    ticks: {
                        fontSize: 2,
                        fontColor: 'transparent'
                    }
                }],
            yAxes: [{
                    display: false,
                    ticks: {
                        display: false,
                        min: 35,
                        max: 89
                    }
                }]
        },
        elements: {
            line: {
                borderWidth: 1
            },
            point: {
                radius: 4,
                hitRadius: 10,
                hoverRadius: 4
            }
        }
    }
});
var cardChart2 = new Chart($('#card-chart2'), {
    type: 'line',
    data: {
        labels: ['January', 'February', 'March', 'April', 'May', 'June', 'July'],
        datasets: [{
                backgroundColor: getStyle('--info'),
                borderColor: 'rgba(255,255,255,.55)',
                data: [1, 18, 9, 17, 34, 22, 11]
            }]
    },
    options: {
        maintainAspectRatio: false,
        legend: {
            display: false
        },
        scales: {
            xAxes: [{
                    gridLines: {
                        color: 'transparent',
                        zeroLineColor: 'transparent'
                    },
                    ticks: {
                        fontSize: 2,
                        fontColor: 'transparent'
                    }
                }],
            yAxes: [{
                    display: false,
                    ticks: {
                        display: false,
                        min: -4,
                        max: 39
                    }
                }]
        },
        elements: {
            line: {
                tension: 0.00001,
                borderWidth: 1
            },
            point: {
                radius: 4,
                hitRadius: 10,
                hoverRadius: 4
            }
        }
    }
});
var cardChart3 = new Chart($('#card-chart3'), {
    type: 'line',
    data: {
        labels: ['January', 'February', 'March', 'April', 'May', 'June', 'July'],
        datasets: [{
                backgroundColor: 'rgba(255,255,255,.2)',
                borderColor: 'rgba(255,255,255,.55)',
                data: [78, 81, 80, 45, 34, 12, 40]
            }]
    },
    options: {
        maintainAspectRatio: false,
        legend: {
            display: false
        },
        scales: {
            xAxes: [{
                    display: false
                }],
            yAxes: [{
                    display: false
                }]
        },
        elements: {
            line: {
                borderWidth: 2
            },
            point: {
                radius: 0,
                hitRadius: 10,
                hoverRadius: 4
            }
        }
    }
});
var cardChart4 = new Chart($('#card-chart4'), {
    type: 'bar',
    data: {
        labels: ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December', 'January', 'February', 'March', 'April'],
        datasets: [{
                backgroundColor: 'rgba(255,255,255,.2)',
                borderColor: 'rgba(255,255,255,.55)',
                data: [78, 81, 80, 45, 34, 12, 40, 85, 65, 23, 12, 98, 34, 84, 67, 82]
            }]
    },
    options: {
        maintainAspectRatio: false,
        legend: {
            display: false
        },
        scales: {
            xAxes: [{
                    display: false,
                    barPercentage: 0.6
                }],
            yAxes: [{
                    display: false
                }]
        }
    }
});
var mainChart = new Chart($('#main-chart'), {
    type: 'line',
    data: {
        labels: ['M', 'T', 'W', 'T', 'F', 'S', 'S', 'M', 'T', 'W', 'T', 'F', 'S', 'S', 'M', 'T', 'W', 'T', 'F', 'S', 'S', 'M', 'T', 'W', 'T', 'F', 'S', 'S'],
        datasets: [{
                label: 'Sale',
                backgroundColor: hexToRgba(getStyle('--primary'), 30),
                borderColor: getStyle('--info'),
                pointHoverBackgroundColor: '#fff',
                borderWidth: 2,
                data: [165, 180, 70, 69, 77, 57, 125, 165, 172, 91, 173, 138, 155, 89, 50, 161, 65, 163, 160, 103, 114, 185, 125, 196, 183, 64, 137, 95, 112, 175]
            }, {
                label: 'Purchase',
                backgroundColor: hexToRgba(getStyle('--success'), 60),
                borderColor: getStyle('--success'),
                pointHoverBackgroundColor: '#fff',
                borderWidth: 2,
                data: [92, 97, 80, 100, 86, 97, 83, 98, 87, 98, 93, 83, 87, 98, 96, 84, 91, 97, 88, 86, 94, 86, 95, 91, 98, 91, 92, 80, 83, 82]
            }]
    },
    options: {
        maintainAspectRatio: false,
        legend: {
            display: false
        },
        scales: {
            xAxes: [{
                    gridLines: {
                        drawOnChartArea: false
                    }
                }],
            yAxes: [{
                    ticks: {
                        beginAtZero: true,
                        maxTicksLimit: 5,
                        stepSize: Math.ceil(200 / 5),
                        max: 200
                    }
                }]
        },
        elements: {
            point: {
                radius: 0,
                hitRadius: 10,
                hoverRadius: 4,
                hoverBorderWidth: 3
            }
        }
    }
});
