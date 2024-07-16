// src/components/StockAutoComplete.tsx

import React, { useState } from 'react';
import Select from 'react-select';
// 1. MSFT
// 2. AAPL
// 3. NVDA
// 4. AMZN
// 5. META
// 6. GOOGL
// 7. GOOG
// 8. BRK.B
// 9. LLY
// 10. AVGO
const stockSymbols = [
    { value: 'AAPL', label: 'AAPL' },
    { value: 'MSFT', label: 'MSFT' },
    { value: 'IBM', label: 'IBM' },
    { value: 'NVDA', label: 'NVDA' },
    { value: 'AMZN', label: 'AMZN' },
    { value: 'META', label: 'META' },
    { value: 'GOOGL', label: 'GOOGL' },
    { value: 'GOOG', label: 'GOOG' },
    { value: 'BRK.B', label: 'BRK.B' },
    { value: 'LLY', label: 'LLY' },
    { value: 'AVGO', label: 'AVGO' },
];

interface StockAutoCompleteProps {
    onSelect: (symbols: string[]) => void;
  }
  
  const StockAutoComplete: React.FC<StockAutoCompleteProps> = ({ onSelect }) => {
    const [selectedOptions, setSelectedOptions] = useState<{ value: string, label: string }[]>([]);
  
    const handleChange = (options: any) => {
      const selectedSymbols = options ? options.map((option: { value: string }) => option.value) : [];
      setSelectedOptions(options);
      onSelect(selectedSymbols);
    };
  
    return (
      <Select
        isMulti
        options={stockSymbols}
        value={selectedOptions}
        onChange={handleChange}
        placeholder="Enter stock symbols"
      />
    );
  };
  
  export default StockAutoComplete;