import React from 'react';

import userIcon from '../../images/user.png';
import passwordIcon from '../../images/password.png';

import styles from '../../styles/SignIn.module.css';

function SignIn() {
    return (
        <div className={styles.container}>
            <div className={styles.containerItem}>
                <div className={styles.inputContainer}>
                    <img src={userIcon} alt="userIcon" className={styles.inputItemImg}></img>
                    <input placeholder="아이디" className={styles.inputItem}></input>
                </div>
                <div className={styles.lastInputContainer}>
                    <img src={passwordIcon} alt="passwordIcon" className={styles.inputItemImg}></img>
                    <input placeholder="비밀번호" className={styles.inputItem}></input>
                </div>
            </div >

        </div>
    );
}

export default SignIn;