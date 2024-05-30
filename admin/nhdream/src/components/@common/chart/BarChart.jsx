import React, { useEffect, useRef } from "react";
import { Chart, registerables } from "chart.js";

const BarChart = ({flowData, maxValue}) => {
    const chartRef = useRef(null);
    let chartInstance = null;

    const options =  {
        maintainAspectRatio: false, // 차트의 종횡비를 유지하지 않습니다.
        responsive: false, // 차트가 브라우저 크기에 따라 반응적으로 크기를 조절합니다.
        scales: {
            y: {
                beginAtZero: true,
                max: maxValue,
            },
            x:{
                grid: {
                    display: false // x 축의 눈금선을 숨김
                },
                ticks: {
                    stepSize: 2 // 간격 설정
                }
            }
        },
        plugins: {
            legend: {
                align: 'end'
            }
        }
    };

    

    useEffect(() => {
        const ctx = chartRef.current.getContext("2d");

        const createChart = () => {
        Chart.register(...registerables);

        // flowData를 사용하여 차트 데이터 구성
        const labels = flowData.map(item => item.referenceDate);
        const mintData = flowData.map(item => item.mintAmount);
        const burnData = flowData.map(item => item.burnAmount);
        chartInstance = new Chart(ctx, {
            type: "bar",
            data: {
                labels: labels,
                datasets: [
                    {
                        label: '발행량',
                        data: mintData, // 두 번째 바의 데이터
                        backgroundColor: 'rgba(54, 162, 235, 0.2)',
                        borderColor: 'rgb(54, 162, 235)',
                        borderWidth: 1
                    },
                    {
                        label: '소각량',
                        data: burnData,
                        backgroundColor: 'rgba(255, 99, 132, 0.2)',
                        borderColor: 'rgb(255, 99, 132)',
                        borderWidth: 1
                    },
                ],
            },
            options: options
        });
        };

        const destroyChart = () => {
            if (chartInstance) {
                chartInstance.destroy();
                chartInstance = null;
            }
        };

        destroyChart(); // 기존 차트 파괴
        createChart(); // 새로운 차트 생성

        return () => {
        destroyChart(); // 컴포넌트가 unmount될 때 차트 파괴
        };
    }, [flowData]);

    return <canvas width="930" height="220" ref={chartRef} />;
    };

export default BarChart;