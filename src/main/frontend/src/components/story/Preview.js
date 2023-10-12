import React, { useState, useRef } from "react";
import upload from '../../images/upload.png';

const Preview = ({ setImageFiles }) => {
    const [imageSrcs, setImageSrcs] = useState([]);
    const fileInputRef = useRef(null);

    const handleImageClick = () => {
        fileInputRef.current.click();
    };

    const onUpload = (e) => {
        const files = Array.from(e.target.files);
        const updatedImageSrcs = [...imageSrcs];
        const promises = files.map(file => {
            return new Promise((resolve, reject) => {
                const reader = new FileReader();
                reader.readAsDataURL(file);
                reader.onload = () => {
                    updatedImageSrcs.push(reader.result);
                    resolve();
                };
                reader.onerror = () => reject();
            });
        });

        Promise.all(promises).then(() => {
            setImageSrcs(updatedImageSrcs);
            setImageFiles(files);
        });
    };

    return (
        <div>
            <input
                accept="image/*"
                type="file"
                multiple
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
                    flexDirection: "column",
                    alignItems: "center",
                    justifyContent: "center"
                }}
            >
                {imageSrcs.length > 0 ? (
                    imageSrcs.map((src, index) => (
                        <img
                            key={index}
                            width={'400px'}
                            height={'400px'}
                            src={src}
                            alt={`Preview ${index + 1}`}
                            style={{borderRadius: "8px", marginBottom: "10px"}}
                        />
                    ))
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
