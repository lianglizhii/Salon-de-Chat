import {useState} from "react";
import {useNavigate} from "react-router-dom";
import axios from 'axios';
import {Form, Input, Button} from "antd";

const Login = () => {
    const [mail, setMail] = useState('')
    const [password, setPassword] = useState('')
    const navigate = useNavigate();

    const handleLogin =async (event) => {
        event.preventDefault();
        console.log("mail = " + mail)
        console.log("password = " + password)

        try {
            const response = await axios.post('http://localhost:8080/api/login', {
                mail: mail,
                password: password
            });

            if (response && response.data) {
                // traitement de la réponse
                console.log("success login");
                sessionStorage.setItem('user', JSON.stringify(response.data));
                // redirect to the chat list page
                navigate("/rooms");
            } else {
                // traitement de l'erreur
                console.log("fail login");
            }
        } catch (error) {
            // traitement de l'erreur
            console.error("fail login:", error);
        }
    }

    return (
        <div className="login-container" style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh' }}>
            <Form>
                <h1 style={{ textAlign: 'center', marginBottom: '24px' }}>Connexion</h1>
                <Form.Item label="Email" name="mail" rules={[{ required: true, message: 'Please input your email' }]}>
                    <Input value={mail} onChange={(e) => setMail(e.target.value)} />
                </Form.Item>
                <Form.Item label="Password" name="password" rules={[{ required: true, message: 'Please input your password' }]}>
                    <Input.Password value={password} onChange={(e) => setPassword(e.target.value)} />
                </Form.Item>
                <Form.Item>
                    <Button type="primary" block onClick={handleLogin}>
                        Connexion
                    </Button>
                </Form.Item>
            </Form>
        </div>
    );
}

export default Login;
