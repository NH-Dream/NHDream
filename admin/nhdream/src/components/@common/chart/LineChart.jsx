import React, { useEffect, useRef } from "react";
import {
    Chart,
    LineController,
    CategoryScale,
    LinearScale,
    PointElement,
    LineElement,
} from "chart.js";

const LineChart = ({tradeData, tradeMaxValue}) => {
    const chartRef = useRef(null);
    let chartInstance = null;

    const options = {
        maintainAspectRatio: false,
        responsive: false,
        scales: {
            y: {
                beginAtZero: true,
                max: tradeMaxValue,
            },
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
        Chart.register(
            LineController,
            CategoryScale,
            LinearScale,
            PointElement,
            LineElement
        );

        const labels = tradeData.map(item => item.referenceDate);
        const tradeAmount = tradeData.map(item => item.tradeAmount);

        chartInstance = new Chart(ctx, {
            type: "line",
            data: {
            labels: labels,
            datasets: [
                {
                label: "최근 거래량",
                data: tradeAmount,
                borderColor: "rgba(255, 99, 132, 1)",
                backgroundColor: "rgba(255, 99, 132, 0.2)",
                pointRadius: 5, // 포인트 크기
                pointBackgroundColor: "rgba(255, 99, 132, 1)", // 포인트 배경색
                pointBorderColor: "rgba(255, 255, 255, 1)", // 포인트 테두리 색
                pointHoverRadius: 7, // 호버 시 포인트 크기
                pointHoverBackgroundColor: "rgba(255, 99, 132, 1)", // 호버 시 포인트 배경색
                pointHoverBorderColor: "rgba(255, 255, 255, 1)", // 호버 시 포인트 테두리 색
                fill: false, // 라인 그래프에서 영역 채우기 비활성화
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

        const initializeChart = () => {
        destroyChart(); // 이전 차트 파괴
        createChart(); // 새로운 차트 생성
        };

        // 컴포넌트가 처음 렌더링될 때 차트 초기화
        initializeChart();

        // 컴포넌트가 unmount될 때 차트 파괴
        return () => {
        destroyChart();
        };
    }, [tradeData]);

    return <canvas width="940" height="160" ref={chartRef} />;
};

export default LineChart;