// src/components/StockDropdown.tsx

import React, { useState } from 'react';

interface StockDropdownProps {
  onSelect: (symbol: string) => void;
}

const StockDropdown: React.FC<StockDropdownProps> = ({ onSelect }) => {
  const [selectedSymbol, setSelectedSymbol] = useState<string>('');

  const symbols: string[] = [
    'IBM','MSFT', 'AAPL', 'NVDA', 'AMZN', 'META', 'GOOGL', 'GOOG', 'BRK.B', 'LLY', 'AVGO'
  ];

  const handleChange = (event: React.ChangeEvent<HTMLSelectElement>) => {
    const symbol = event.target.value;
    setSelectedSymbol(symbol);
    onSelect(symbol);
  };

  return (
    <div>
      <label htmlFor="stock-symbol">Select Stock Symbol: </label>
      <select id="stock-symbol" value={selectedSymbol} onChange={handleChange}>
        <option value="">Select a symbol</option>
        {symbols.map((symbol) => (
          <option key={symbol} value={symbol}>
            {symbol}
          </option>
        ))}
      </select>
    </div>
  );
};

export default StockDropdown;
