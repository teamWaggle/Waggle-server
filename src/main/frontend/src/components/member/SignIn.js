import React from 'react';

import userIcon from '../../images/user.png';
import passwordIcon from '../../images/password.png';

import styles from '../../styles/SignIn.module.css';

function SignIn({ updateFormData }) {

    const handleChange = (e) => {
        const { name, value } = e.target;
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
                </div>
                <div className={styles.lastInputContainer}>
                    <img src={passwordIcon} alt="passwordIcon" className={styles.inputItemImg}></img>
                    <input 
                    name="password"
                    placeholder="비밀번호" 
                    className={styles.inputItem} 
                    type="password"
                    onChange={handleChange}
                    ></input>
                </div>
            </div >

        </div>
    );
}

export default SignIn;