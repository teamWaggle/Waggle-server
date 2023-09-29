import React from 'react';
import styles from '../styles/ImageComponent.module.css';


function ImageComponent({ filename, className, onClick }) {
  return (
    <img src={`/images/${filename}`} alt="Description" className={className} onClick={onClick} />
  );
}

export default ImageComponent;