import React, { useState, useEffect } from 'react';
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from 'recharts';
import StockDropdown from './StockDropdown.tsx';
import StockAutoComplete from './StockAutoComplete.tsx';
import { fetchStockDataForWeek } from '../services/StockDataService.tsx';

interface StockInfoDTO {
  open_price: number;
  close_price: number;
  high_price: number;
  low_price: number;
  volume: number;
}
const StockDashboard: React.FC = () => {
  const [stockData, setStockData] = useState<StockInfoDTO | null>(null);
  const [selectedSymbol, setSelectedSymbol] = useState<string>('');
  const [startDate, setStartDate] = useState<string>('');
  const [message, setMessage] = useState<string>('');
  const [selectedDate, setSelectedDate] = useState<string>('');
  const [symbol, setSymbol] = useState<string>('AAPL');
  const [weeklyData, setWeeklyData] = useState<StockInfoDTO[]>([]);
  const [statistics, setStatistics] = useState<{ average_close: number, max_close: number, min_close: number, average_high: number, max_high: number, min_high: number, average_low: number, max_low: number, min_low: number, average_open: number, max_open: number, min_open: number, average_volume: number, max_volume: number, min_volume: number } | null>(null);


  const handleSymbolSelect = (symbol: string) => {
    setSelectedSymbol(symbol);
    console.log('Selected symbol:', symbol);
    // Add your logic to handle the selected symbol
  };

  // useEffect(() => {
  //   if (selectedSymbol && startDate) {
  //     fetchAndCalculate(selectedSymbol, startDate);
  //   }
  // }, [selectedSymbol, startDate]);

  const getWeekRange = (date: Date) => {
    // Check if it is Monday, or find first Monday before the date
    let day = date.getDay();
    console.log('Day:', day);
    let diff = date.getDate() - (day===6?-1:day%6) + 1;
    const firstDay = new Date(date.setDate(diff));
    console.log('First day:', firstDay);
    //check if it is Friday, or find the next Friday
    day = date.getDay();
    diff = date.getDate() + 4 ;
    const lastDay = new Date(date.setDate(diff));
    console.log('First day:', firstDay, 'Last day:', lastDay);
    return { firstDay, lastDay };
  }

  const calculateStatistics = (daily: StockInfoDTO[]) => {
    const filteredDaily = daily.filter(day => day.low_price !== undefined);

    for (let i = 0; i < filteredDaily.length; i++) {
      const day = filteredDaily[i];
      console.log('Day:', day);
    }

    // const average = filteredPrices.reduce((a, b) => a + b, 0) / filteredPrices.length;
    // const max = Math.max(...filteredPrices);
    // const min = Math.min(...filteredPrices);
    const average_close = filteredDaily.reduce((a, b) => a + b.close_price, 0) / filteredDaily.length;
    const max_close = Math.max(...filteredDaily.map(daily => daily.close_price));
    const min_close = Math.min(...filteredDaily.map(daily => daily.close_price));
    const average_high = filteredDaily.reduce((a, b) => a + b.high_price, 0) / filteredDaily.length;
    const max_high = Math.max(...filteredDaily.map(daily => daily.high_price));
    const min_high = Math.min(...filteredDaily.map(daily => daily.high_price));
    const average_low = filteredDaily.reduce((a, b) => a + b.low_price, 0) / filteredDaily.length;
    const max_low = Math.max(...filteredDaily.map(daily => daily.low_price));
    const min_low = Math.min(...filteredDaily.map(daily => daily.low_price));
    const average_open = filteredDaily.reduce((a, b) => a + b.open_price, 0) / filteredDaily.length;
    const max_open = Math.max(...filteredDaily.map(daily => daily.open_price));
    const min_open = Math.min(...filteredDaily.map(daily => daily.open_price));
    const average_volume = filteredDaily.reduce((a, b) => a + b.volume, 0) / filteredDaily.length;
    const max_volume = Math.max(...filteredDaily.map(daily => daily.volume));
    const min_volume = Math.min(...filteredDaily.map(daily => daily.volume));
    
    return {
      average_close,
      max_close,
      min_close,
      average_high,
      max_high,
      min_high,
      average_low,
      max_low,
      min_low,
      average_open,
      max_open,
      min_open,
      average_volume,
      max_volume,
      min_volume
  };
};

  const fetchAndCalculate = async (symbol: string, sdate: string) => {
    console.log('Fetching data for:', symbol,sdate);

    console.log('Selected date:', sdate);
    const date = new Date(sdate);
    const { firstDay, lastDay } = getWeekRange(date);
    console.log('First day:', firstDay, 'Last day:', lastDay);
    const weekDates: string[] = [];
    firstDay.setDate(firstDay.getDate() -1)
    for (let d = firstDay; d < lastDay; d.setDate(d.getDate()+1)) {
      weekDates.push(new Date(d).toISOString().split('T')[0]);
    }

    const weekData = await fetchStockDataForWeek(symbol, weekDates);
    setWeeklyData(weekData);

    
    const stats = calculateStatistics(weekData);
    setStatistics(stats);
  };

  
 
  // const fetchStockData = async (symbol: string, date: string) => {
  //   try {
  //     const response = await fetch(`http://localhost:8080/api/stock_data/${symbol}/${date}`);
  //     const data: StockInfoDTO = await response.json();
      
  //     console.log('Fetched stock data:', data);
  //     setStockData(data);
  //   } catch (error) {
  //     console.error('Error fetching stock data:', error);
  //     setMessage('Error fetching stock data');
  //   }
  // };
  const handleDateChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setStartDate(event.target.value);
    setSelectedDate(event.target.value);
  };

  const createDatabase = async () => {
    try {
      const response = await fetch('http://localhost:8080/api/db_create', {
        method: 'POST',
      });
      const data = await response.text();
      setMessage(data);
    } catch (error) {
      console.error('Error creating database:', error);
      setMessage('Error creating database');
    }
  };

  const collectData = async () => {
    try {
      const response = await fetch('http://localhost:8080/api/collect_data', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          start_date: startDate,
          symbols: selectedSymbols
        }),
      });
      const data = await response.json();
      setMessage(`Data collected. Inserted: ${data.rowsInserted}, TotalRows: ${data.totalRows}`);
    } catch (error) {
      console.error('Error collecting data:', error);
      setMessage('Error collecting data');
    }
  };


  const [selectedSymbols, setSelectedSymbols] = useState<string[]>([]);

  const handleSymbolSelectList = (symbols: string[]) => {
    setSelectedSymbols(symbols);
    console.log('Selected symbols:', symbols);
  };

  return (
    <div className="p-4 max-w-4xl mx-auto">
      <h1 className="text-2xl font-bold mb-4">Stock Price Analytics Dashboard</h1>
      <div className="mb-4">
        <button onClick={createDatabase} className="mr-2 p-2 bg-blue-500 text-white rounded">Create Database</button>
        <div className="mb-5">
          <h1>Stock Selector</h1>
          <StockAutoComplete onSelect={handleSymbolSelectList} />
          {selectedSymbol && <p>Selected Symbol: {selectedSymbol}</p>}
        </div>
        <button onClick={collectData} className="mr-2 p-2 bg-green-500 text-white rounded">Collect Data</button>
        <input 
          type="date" 
          value={startDate} 
          onChange={handleDateChange}
          className="ml-4 p-2 border rounded"
        />
      </div>
      <div>
        <StockDropdown onSelect={handleSymbolSelect} />
        <button onClick={() => fetchAndCalculate(selectedSymbol, selectedDate)} className="p-2 bg-blue-500 text-white rounded">Fetch Data</button>
      </div>
      {message && <p className="mb-4 text-red-500">{message}</p>}
      {statistics ? (
        <div>
          <h2 className="text-4xl font-bold mb-4">Stock Data</h2>
          <table className="min-w-full bg-white">
            <tbody>
              <tr>
                <td className="py-2 px-4 border">Average High Price:</td>
                <td className="py-2 px-4 border">{statistics.average_high.toFixed(2)}</td>
              </tr>
              <tr>
                <td className="py-2 px-4 border">Max High Price:</td>
                <td className="py-2 px-4 border">{statistics.max_high.toFixed(2)}</td>
              </tr>
              <tr>
                <td className="py-2 px-4 border">Min High Price:</td>
                <td className="py-2 px-4 border">{statistics.min_high.toFixed(2)}</td>
              </tr>
              <tr>
                <td className="py-2 px-4 border">Average Low Price:</td>
                <td className="py-2 px-4 border">{statistics.average_low.toFixed(2)}</td>
              </tr>
              <tr>
                <td className="py-2 px-4 border">Max Low Price:</td>
                <td className="py-2 px-4 border">{statistics.max_low.toFixed(2)}</td>
              </tr>
              <tr>
                <td className="py-2 px-4 border">Min Low Price:</td>
                <td className="py-2 px-4 border">{statistics.min_low.toFixed(2)}</td>
              </tr>
              <tr>
                <td className="py-2 px-4 border">Average Open Price:</td>
                <td className="py-2 px-4 border">{statistics.average_open.toFixed(2)}</td>
              </tr>
              <tr>
                <td className="py-2 px-4 border">Max Open Price:</td>
                <td className="py-2 px-4 border">{statistics.max_open.toFixed(2)}</td>
              </tr>
              <tr>
                <td className="py-2 px-4 border">Min Open Price:</td>
                <td className="py-2 px-4 border">{statistics.min_open.toFixed(2)}</td>
              </tr>
              <tr>
                <td className="py-2 px-4 border">Average Close Price:</td>
                <td className="py-2 px-4 border">{statistics.average_close.toFixed(2)}</td>
              </tr>
              <tr>
                <td className="py-2 px-4 border">Max Close Price:</td>
                <td className="py-2 px-4 border">{statistics.max_close.toFixed(2)}</td>
              </tr>
              <tr>
                <td className="py-2 px-4 border">Min Close Price:</td>
                <td className="py-2 px-4 border">{statistics.min_close.toFixed(2)}</td>
              </tr>
              <tr>
                <td className="py-2 px-4 border">Average Volume:</td>
                <td className="py-2 px-4 border">{statistics.average_volume.toFixed(2)}</td>
              </tr>
              <tr>
                <td className="py-2 px-4 border">Max Volume:</td>
                <td className="py-2 px-4 border">{statistics.max_volume.toFixed(2)}</td>
              </tr>
              <tr>
                <td className="py-2 px-4 border">Min Volume:</td>
                <td className="py-2 px-4 border">{statistics.min_volume.toFixed(2)}</td>
              </tr>
            </tbody>
          </table>
        </div>
      

      ) : (
        <p>Select a stock symbol and date to view data.</p>
      )}
    </div>
  );
};

export default StockDashboard;