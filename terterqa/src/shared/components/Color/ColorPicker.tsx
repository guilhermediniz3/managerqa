// ColorPicker.tsx
import React, { useState } from 'react';
import { SketchPicker } from 'react-color';

interface ColorPickerProps {
  onColorChange: (color: string) => void;
}

const ColorPicker: React.FC<ColorPickerProps> = ({ onColorChange }) => {
  const [displayColorPicker, setDisplayColorPicker] = useState(false);
  const [color, setColor] = useState('#86168a'); // Cor inicial

  const handleClick = () => setDisplayColorPicker(!displayColorPicker);
  const handleClose = () => setDisplayColorPicker(false);
  const handleChange = (color: any) => {
    setColor(color.hex);
    onColorChange(color.hex);
  };

  return (
    <div>
      <button onClick={handleClick}>Escolher Cor</button>
      {displayColorPicker ? (
        <div>
          <div onClick={handleClose} />
          <SketchPicker color={color} onChange={handleChange} />
        </div>
      ) : null}
    </div>
  );
};

export default ColorPicker;
