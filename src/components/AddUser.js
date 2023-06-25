import React, { useState, useEffect } from "react";
import axios from 'axios';
import { Modal, Form, Select, Button } from 'antd';
const AddUser = ({currentChat,exitAddUser}) => {
    const [form] = Form.useForm();
    const [modalVisible, setModalVisible] = useState(true);
    const [user, setUser] = useState([]);
    const [selectedUsers, setSelectedUsers] = useState([]);

    useEffect(() => {
        console.log("currentChat:", currentChat);
        getUser();
    }, []);
    const handleSubmit = async () => {
        try {
            const response = await axios.post(`http://localhost:8080/api/rooms/inviter/${currentChat.id}`, {
                    users_id: selectedUsers,
            });
            console.log("Invitation réussie:", response.data);
            alert("Invitation réussie!")
            closeModal();
        } catch (error) {
            console.error("邀请失败", error);
        }
    };

    // Obtenir tous les utilisateurs qui n'ont pas rejoint ce salon de discussion
    const getUser = async () => {
        try {
            const response = await axios.get(`http://localhost:8080/api/rooms/inviter/${currentChat.id}`);
            console.log("获得用户成功:", response.data);
            setUser(response.data);
        } catch (error) {
            console.error("获得用户失败", error);
        }
    };
    const openModal = () => {
        setModalVisible(true);
    };
    const closeModal = () => {
        setModalVisible(false);
        exitAddUser();
    };

    const handleChange = (selectedItems) => {
        setSelectedUsers(selectedItems);
    }

    return (
        <div >
            {modalVisible && (
            <Modal
                title="ajouter des utilisateurs"
                open={modalVisible}
                onCancel={closeModal}
                footer={null}
            >
                <Form form={form} onFinish={handleSubmit}>
                    <Form.Item>
                        <Select
                            mode="multiple"
                            placeholder={`Sélectionnez les utilisateurs à inviter`}
                            onChange={handleChange}
                            footer={null}
                        >
                            {user.map((item) => (
                                <Select.Option key={item.id} value={item.id}>
                                    {item.mail}
                                </Select.Option>
                            ))}
                        </Select>
                    </Form.Item>
                    <Form.Item>
                        <Button type="primary" htmlType="submit" >
                            Ajouter
                        </Button>
                    </Form.Item>
                </Form>
            </Modal>
            )}
        </div>
    );
}

export default  AddUser;