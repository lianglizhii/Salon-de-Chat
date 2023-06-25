import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { Layout, Menu, Card } from "antd";
import axios from 'axios';

import { ChatListSalons } from "./ChatListContent";
import Planifier from "./Planifier";

const { Header, Content, Sider } = Layout;

const ChatList = () => {
    const [chats, setChats] = useState([]);
    const [User, setUser] = useState([]);
    const navigate = useNavigate();
    const [selectedNavItem, setSelectedNavItem] = useState('accueil');

    // Fonction pour récupérer la liste des canaux depuis l'API
    const fetchCanalList = async (url) => {
        try {
            const userid = User.id; // Récupérer l'ID de l'utilisateur
            const response = await axios.get(url, {
                params: {
                    user_Id: userid
                }
            });
            setChats(response.data);
        } catch (error) {
            console.error('Error fetching Canal list:', error);
        }
    }

    // Fonction de déconnexion
    const handleLogout = () => {
        sessionStorage.removeItem('user');
        navigate("/");
    };

    // Effet de chargement initial pour vérifier si l'utilisateur est connecté
    useEffect(() => {
        const user = sessionStorage.getItem('user');

        if (user) {
            const parsedUser = JSON.parse(user);
            setUser(parsedUser);
        } else {
            navigate("/");
        }
    }, [])

    return (
        <Layout style={{ minHeight: "100vh" }} >
            <Sider collapsible={false} theme="light" >
                <div style={{ display: 'flex', flexDirection: 'column', height: '100%' }}>
                    <div>
                <Menu theme="light" mode="vertical" selectedKeys={[selectedNavItem]}>
                    <Menu.Item key="accueil" onClick={() => { setSelectedNavItem("accueil"); }}>
                        <b>Accueil</b>
                    </Menu.Item>
                    <Menu.Item key="planifier" onClick={() => { setSelectedNavItem("planifier"); }}>
                        Planifier une discussion
                    </Menu.Item>
                    <Menu.Item key="Salons" onClick={() => { fetchCanalList('http://localhost:8080/api/rooms/owner'); setSelectedNavItem("Salons"); }}>
                        Mes salons de discussion
                    </Menu.Item>
                    <Menu.Item key="invitations" onClick={() => { fetchCanalList('http://localhost:8080/api/rooms/invitation'); setSelectedNavItem("invitations"); }}>
                        Mes invitations
                    </Menu.Item>
                </Menu>
                </div>
                <div style={{marginTop: 'auto' }}>
                <Card
                    title="Information"
                    bordered={true}
                >
                    <p><b>ID:</b> {User && User.id}</p>
                    <p><b>Name:</b> {User && User.firstName} {User && User.lastName}</p>
                    <p><b>Email:</b> {User && User.mail}</p>
                </Card>
                </div>
                </div>
            </Sider>
            <Layout>
                <Header >
                    <div className="logo" style={{ width:'800'}}/>
                    <div style={{ display: 'flex'}}>
                        <h2 style={{ color: 'white'}}>Salon de chat</h2>
                        <div style={{ marginLeft: 'auto' }}></div> {                               }

                        <div>
                        <Menu  theme="dark" mode="horizontal" defaultSelectedKeys={["3"]} style={{ justifyContent: 'flex-end'}}>
                            <Menu.Item key="3"  onClick={handleLogout}>Déconnexion</Menu.Item>
                        </Menu>
                        </div>
                    </div>

                </Header>
                <Content style={{ margin: "16px", height: "100%" }}>
                    {selectedNavItem === 'Salons' && (
                        <ChatListSalons chats={chats} User={User} state={'salons'}/>
                    )}

                    {selectedNavItem === 'invitations' && (
                        <ChatListSalons chats={chats} User={User} state={'invitations'}/>
                    )}

                    {selectedNavItem === 'accueil' && (

                        <div style={{ display: 'flex' }}>

                            <p>
                                <span style={{ display: 'block' }}><b>Bienvenue sur la page d'accueil</b> </span>
                            </p>

                        </div>
                    )}
                    {selectedNavItem === 'planifier' && (
                        <Planifier Userid={User.id} exit={() => setSelectedNavItem(null)}/>
                    )}
                </Content>
            </Layout>
        </Layout>
    );
}

export default ChatList;
