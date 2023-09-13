import React from 'react';

import userIcon from '../../images/user.png';
import passwordIcon from '../../images/password.png';
import birthdayIcon from '../../images/birthday.png';
import phoneIcon from '../../images/phone.png';
import pawIcon from '../../images/paw.png';


import styles from '../../styles/SignUp.module.css';

function SignUp() {
    return (
        <div className={styles.container}>
            <div className={styles.containerItem}>

                <div className={styles.inputContainer}>
                    <img src={userIcon} alt="userIcon" className={styles.inputItemImg}></img>
                    <input placeholder="아이디" className={styles.inputItem}></input>
                    <button className={styles.checkButton}>확인</button>
                </div>
                <div className={styles.inputContainer}>
                <img src={passwordIcon} alt="passwordIcon" className={styles.inputItemImg}></img>
                    <input placeholder="비밀번호" className={styles.inputItem}></input>
                </div>
                <div className={styles.inputContainer}>
                <img src={userIcon} alt="userIcon" className={styles.inputItemImg}></img>
                    <input placeholder="이름" className={styles.inputItem}></input>
                </div>
                <div className={styles.inputContainer}>
                <img src={birthdayIcon} alt="birthdayIcon" className={styles.inputItemImg}></img>
                    <input placeholder="생년월일 8자리" className={styles.inputItem}></input>
                </div >
                <div className={styles.lastInputContainer}>
                <img src={phoneIcon} alt="phoneIcon" className={styles.inputItemImg}></img>
                    <input placeholder="휴대전화번호" className={styles.inputItem}></input>
                </div >
            </div >

            <div className={styles.containerItem}>
                <div className={styles.inputContainer}>
                <img src={userIcon} alt="userIcon" className={styles.inputItemImg}></img>
                    <input placeholder="주소 (선택)" className={styles.inputItem}></input>
                </div>
                <div className={styles.inputContainer}>
                <img src={userIcon} alt="userIcon" className={styles.inputItemImg}></img>
                    <input placeholder="프로필 사진 (선택)" className={styles.inputItem}></input>
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