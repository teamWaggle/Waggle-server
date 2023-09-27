import React from 'react';

import userIcon from '../../images/user.png';
import passwordIcon from '../../images/password.png';
import birthdayIcon from '../../images/birthday.png';
import phoneIcon from '../../images/phone.png';
import pawIcon from '../../images/paw.png';


import styles from '../../styles/SignUp.module.css';

function SignUp({ updateFormData }) {

    const handleChange = (e) => {
        const { name, type } = e.target;
        let value;
        
        if(type === 'file') {
            value = e.target.files[0];
        } else {
            value = e.target.value;
        }
        
        updateFormData(name, value);
    };
    

    return (
        <div className={styles.container}>
            <div className={styles.containerItem}>

                <div className={styles.inputContainer}>
                    <img src={userIcon} alt="userIcon" className={styles.inputItemImg}></img>
                    <input
                        name="username"
                        placeholder="아이디"
                        className={styles.inputItem}
                        onChange={handleChange}
                    ></input>
                    <button className={styles.checkButton}>확인</button>
                </div>
                <div className={styles.inputContainer}>
                    <img src={passwordIcon} alt="passwordIcon" className={styles.inputItemImg}></img>
                    <input
                        name="password"
                        placeholder="비밀번호"
                        className={styles.inputItem}
                        type="password"
                        onChange={handleChange}
                    ></input>
                </div>
                <div className={styles.inputContainer}>
                    <img src={userIcon} alt="userIcon" className={styles.inputItemImg}></img>
                    <input
                        name="nickname"
                        placeholder="이름"
                        className={styles.inputItem}
                        onChange={handleChange}
                    ></input>
                </div>
                <div className={styles.lastInputContainer}>
                    <img src={phoneIcon} alt="phoneIcon" className={styles.inputItemImg}></img>
                    <input
                        name="phone"
                        placeholder="휴대전화번호"
                        className={styles.inputItem}
                        onChange={handleChange}
                    ></input>
                </div >
            </div >

            <div className={styles.containerItem}>
                <div className={styles.inputContainer}>
                    <img src={userIcon} alt="userIcon" className={styles.inputItemImg}></img>
                    <input placeholder="주소 (선택)" className={styles.inputItem}></input>
                </div>
                <div className={styles.inputContainer}>
                    <img src={userIcon} alt="userIcon" className={styles.inputItemImg}></img>
                    <input 
                    name="profileImg"
                    placeholder="프로필 사진 (선택)" 
                    type="file"
                    accept="image/*"
                    className={styles.inputItem}
                    onChange={handleChange}
                    ></input>
                </div>
                <div className={styles.lastInputContainer}>
                    <img src={pawIcon} alt="userIcon" className={styles.inputItemImg}></img>
                    <input placeholder="반려동물 (선택)" className={styles.inputItem}></input>
                </div>
            </div>
        </div>
    );
}

export default SignUp;