import React from 'react';

import styles from '../../styles/SignInButton.module.css';

function SignInButton({ onClick }) {
    return (
        <button className={styles.button} onClick={onClick}>
            로그인
        </button>
    );
}

export default SignInButton;