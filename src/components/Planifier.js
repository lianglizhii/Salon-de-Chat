import React, { useState } from "react";
import axios from 'axios';
import {Modal, Form, Input, Button} from 'antd';
const Planifier = ({Userid,exit}) => {
    console.log("Userid:", Userid);
    const [form] = Form.useForm();
    const [modalVisible, setModalVisible] = useState(true);
    const handleSubmit = async (valus) => {
        console.log("valus:", valus);
        console.log("Userid:", Userid);
        try {
            const response = await axios.post('http://localhost:8080/api/rooms/planifier', {
                    user_id: Userid,
                    canal_name: valus.canalName,
                    canal_description: valus.canalDescription,
                    canal_date: valus.canalDate,
                    canal_time: parseInt(valus.canalTime)
            });

            console.log("Vous avez réussi à ajouter une salle de discussion!", response.data);
            alert("Vous avez réussi à ajouter une salle de discussion!");
            closeModal();
        } catch (error) {
            console.error("error:", error);
        }
    }

    const openModal = () => {
        setModalVisible(true);
    }
    const closeModal = () => {
        exit();
    }

    return (
        <div >
            <Modal
                title="Planifier une discussion"
                open={modalVisible}
                onCancel={closeModal}
                footer={null}
            >
            <Form form={form} onFinish={handleSubmit}>

                <Form.Item name="canalName" label="Nom de la discussion" rules={[{ required: true }]}>
                    <Input style={{ width: '300px' }}/>
                </Form.Item>
                <Form.Item name="canalDescription" label="Description" rules={[{ required: true }]}>
                    <Input style={{ width: '300px' }}/>
                </Form.Item>
                <Form.Item name="canalDate" label="Date" rules={[{ required: true }]}>
                    <Input type="date" style={{ width: '300px' }}/>
                </Form.Item>
                <Form.Item name="canalTime" label="Durée" rules={[{ required: true }]}>
                    <Input style={{ width: '300px' }}/>
                </Form.Item>
                <Form.Item>
                    <Button type="primary" htmlType="submit">
                        Planifier
                    </Button>
                </Form.Item>
            </Form>
            </Modal>
        </div>
    );
}

export default  Planifier;
