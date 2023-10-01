import React from 'react';

function ImageComponent({ filename, className, onClick }) {
  return (
    <img src={`/images/${filename}`} alt="Description" className={className} onClick={onClick} />
  );
}

export default ImageComponent;