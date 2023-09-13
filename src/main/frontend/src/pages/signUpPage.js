import React from 'react';
import { Link } from 'react-router-dom';
import SignUp from '../components/member/SignUp';
import SignUpButton from '../components/member/SignUpButton';

import styles from '../styles/signUpPage.module.css';
import '../styles/globalStyles.css';

function SignUpPage() {
    return (
        <div className="page-container">
            <div className={styles.mainContainer}>
                <Link to="/" className={styles.logo}>
                    <div>Waggle</div>
                </Link>
                <SignUp />
                <SignUpButton />
            </div>
        </div>
    );
}

export default SignUpPage;