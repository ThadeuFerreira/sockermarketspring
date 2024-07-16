// src/services/stockService.ts

import axios from 'axios';

interface StockData {
  date: string;
  open_price: number;
  close_price: number;
  high_price: number;
  low_price: number;
  volume: number;
}

export const fetchStockData = async (symbol: string, date: string): Promise<StockData> => {
  const response = await axios.get(`http://localhost:8080/api/stock_data/${symbol}/${date}`);
  return response.data;
};

export const fetchStockDataForWeek = async (symbol: string, weekDates: string[]): Promise<StockData[]> => {
  console.log('Fetching stock data for week:', weekDates);
  const requests = weekDates.map(date => fetchStockData(symbol, date));
  return Promise.all(requests);
};
