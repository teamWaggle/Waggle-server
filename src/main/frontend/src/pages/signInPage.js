import React, {useState} from 'react';
import { Link, useNavigate } from 'react-router-dom';
import SignIn from '../components/member/SignIn';

import styles from '../styles/signInPage.module.css';
import '../styles/globalStyles.css';
import SignInButton from '../components/member/SignInButton';
import {memberLoginApi} from '../apis/memberApi';

function SignInPage() {
    const navigate = useNavigate();

    const [formData, setFormData] = useState({
        username: '',
        password: '',
    });

    const handleLogin = () => {
        console.log('login button clicked');
        const payload = {
            ...formData
        };

        memberLoginApi(payload)
            .then(response => {
                console.log('Login successful:', response);
                navigate('/');
            })
            .catch(error => {
                console.log('Login failed:', error);
            });
    };

    const updateFormData = (key, value) => {
        setFormData(prev => ({ ...prev, [key]: value }));
    };

    return (
        <div className="page-container">
            <div className={styles.mainContainer}>
                <Link to="/" className={styles.logo}>
                    <div>Waggle</div>
                </Link>
                <SignIn updateFormData={updateFormData} />
                <SignInButton onClick={handleLogin} />
            </div>
        </div>
    );
}

export default SignInPage;