import React, { useState, useRef } from "react";
import upload from '../../images/upload.png';

const Preview = () => {
    const [imageSrc, setImageSrc] = useState(null);
    const fileInputRef = useRef(null);

    const handleImageClick = () => {
        fileInputRef.current.click();
    };

    const onUpload = (e) => {
        const file = e.target.files[0];
        if (file) {
            const reader = new FileReader();
            reader.readAsDataURL(file);

            reader.onload = () => {
                setImageSrc(reader.result);
            };
        }
    };

    return (
        <div>
            <input
                accept="image/*"
                type="file"
                onChange={(e) => onUpload(e)}
                style={{ display: "none" }}
                ref={fileInputRef}
            />
            <div
                onClick={handleImageClick}
                style={{
                    cursor: "pointer",
                    height: "400px",
                    display: "flex",
                    alignItems: "center",
                    justifyContent: "center"
                }}
            >
                {imageSrc ? (
                    <img
                        width={'400px'}
                        height={'400px'}
                        src={imageSrc}
                        alt="Preview"
                        style={{borderRadius: "8px"}}
                    />
                ) : (
                    <img
                        width={'250px'}
                        height={'250px'}
                        src={upload}
                        alt="Upload"
                    />
                )}
            </div>
        </div>
    );
};

export default Preview;
