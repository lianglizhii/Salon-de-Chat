import React, { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import axios from "axios";
import {Modal, Form, Input, Button, Table, Layout, List} from "antd";
import moment from "moment";
import AddUser from "./AddUser";
const { Header, Content, Sider,Footer } = Layout;

const ChatListSalons = ({ chats, User ,state}) => {
    const [ws, setWs] = useState(null);
    const [history, setHistory] = useState("");
    const [message, setMessage] = useState("");
    const [chatUsers, setChatUsers] = useState(null);
    const [inChatRoom, setInChatRoom] = useState(false);
    const [inEditRoom, setInEditRoom] = useState(false);
    const [editedChat, setEditedChat] = useState(null); //Contient les informations de la salle de chat en cours d'édition
    const [currentChat, setCurrentChat] = useState(null);
    const [inUserAdd, setInUserAdd] = useState(false);
    const [currentPage, setCurrentPage] = useState(1); //  Numéro de la page actuelle
    const [pageSize, setPageSize] = useState(5); // Nombre d'éléments affichés par page

    //  Calculer les salles de chat à afficher en fonction du numéro de page et du nombre d'éléments par page
    const startIndex = (currentPage - 1) * pageSize;
    const endIndex = startIndex + pageSize;
    const visibleChats = chats.slice(startIndex, endIndex);

    /*
        Effet de bord qui se déclenche lorsqu'il y a un changement dans la variable "state".
        Il remet le statut "inUserAdd" à false et nettoie les ressources lors du démontage du composant.
        Il ferme la connexion WebSocket si elle est ouverte, réinitialise "history" et met "inChatRoom" à false.
    */
    useEffect(() => {
        setInUserAdd(false);
        return () => {
            // Fermer la connexion WebSocket lorsque le composant est démonté
            setHistory("");
            if (ws) {
                ws.close();
            }
            setInChatRoom(false);
        };
    }, [state]);

    //Fonction qui récupère la liste des utilisateurs de chat pour une salle de chat donnée.
    const fetchChatUsers = async (chat) => {
        try {
            const chatId = chat.id;
            const response = await axios.get(`http://localhost:8080/api/rooms/owner/${chatId}`, {
                params: {
                    chat_Id: chatId
                }
            });
            console.log(response.data);
            setChatUsers(response.data);
        } catch (error) {
            console.error('Error fetching Canal list:', error);
        }
    }

    // Fonction qui handle le changement de page
    const handlePageChange = (page, pageSize) => {
        setCurrentPage(page);
    };

    // Entrer dans la salle de chat
    const enterChatRoom = (chat) => {
        setInUserAdd(false);
        if(ws){
            ws.close();
        }
        setCurrentChat(chat);
        const chatId = chat.id;
        const userId = User.firstName;
        // create a new WebSocket object
        const socket = new WebSocket(`ws://localhost:8080/webSocket/${chatId}/${userId}`);
        setWs(socket);
        // Connection opened
        socket.addEventListener("open", () => {
            console.log("Connection established");
        });
        // Listen for messages
        socket.addEventListener("message", (event) => {
            const receivedMessage = event.data;
            console.log("Receive new message: " + receivedMessage);
            setHistory((prevHistory) => prevHistory + receivedMessage + "\n");
        });
        // Listen for socket closes
        socket.addEventListener("close", () => {
            console.log("Connection closed");
        });
        setInChatRoom(true);
    };

    // Quitter la salle de chat
    const exitChatRoom = () => {
        setHistory("");
        if (ws) {
            ws.close();
        }
        setInChatRoom(false);
    };

    // Fonction qui handle le delete d'une salle de chat
    const handleDelete = async (id) => {
        try {
            // Envoyer une requête de suppression à l'API
            const response = await axios.delete(`http://localhost:8080/api/rooms/owner/${id}`);
            // Traiter la réponse de succès
            if (response.data) {
                alert("resussir de supprimer");
            } else {
                alert("resussir de supprimer!");
            }
        } catch (error) {
            // Traiter la réponse d'erreur
            console.error("erreur de supprimer", error);
        }
    };

    // Fonction qui handle le click sur le bouton "Ajouter un utilisateur"
    const handleSend = () => {
        if (ws && message) {
            ws.send(message);
            setMessage("");
        }
    };

    // Fonction qui handle le click sur le bouton "edit"
    const handleEdit = (chat) => {
        console.log(chat);
        setEditedChat({ ...chat });
        setInEditRoom(true);
        console.log(inEditRoom);
    };

    // Fonction qui handle edition d'une salle de chat
    const handleSave = async (values) => {
        // Envoyer une requête PUT à l'API pour enregistrer les informations de la salle de chat modifiée
        try {
            // Envoyer une requête PUT à l'API pour enregistrer les informations de la salle de chat modifiée
            const response = await axios.put(`http://localhost:8080/api/rooms/owners/${editedChat.id}`, {
                titre: values.titre,
                description: values.description,
                duree: values.duree,
            });
            // Traiter la réponse de succès
            console.log("réussir de modifier:", response.data);
            alert("réussir de modifier!");
        } catch (error) {
            // Traiter la réponse d'erreur
            console.error("erreur de modifier:", error);
        }

        setInEditRoom(false);
    };

    // Fonction de gestion du changement de valeur dans les champs de saisie.
    const handleChange = (e) => {
        const { name, value } = e.target;
        setEditedChat((prevChat) => ({
            ...prevChat,
            [name]: value,
        }));
    };

    // Configuration des colonnes pour le tableau des salons de chat
   const columns_salons = [
       {
           title: "Id",
           dataIndex: "id",
           key: "id",
       },
       {
           title: "Titre",
           dataIndex: "titre",
           key: "titre",
       },
       {
           title: "Description",
           dataIndex: "description",
           key: "description",
       },
       {
           title: "Horaire",
           dataIndex: "horaire",
           key: "horaire",
           render: (horaire) => moment(horaire).format("YYYY-MM-DD"),
       },
       {
           title: "Duree",
           dataIndex: "duree",
           key: "duree",
       },
       {
           title: "Editer",
           key: "edit",
           render: (_, record) => (
               <Button onClick={() => handleEdit(record)}>éditer</Button>
           ),
       },
       {
           title: "Supprimer",
           key: "delete",
           render: (_, record) => (
               <Button onClick={() => handleDelete(record.id)}>supprimer</Button>
           ),
       },
       {
           title: "Entrer",
           key: "enter",
           render: (_, record) => (
               <Button onClick={() => {
                   fetchChatUsers(record);
                   enterChatRoom(record);
               }}>entrer</Button>//
           ),
       },
   ];

    // Configuration des colonnes pour le tableau des invitations
   const columns_invitation = [
       {
           title: 'ID',
           dataIndex: 'id',
           key: 'id',
       },
       {
           title: 'Titre',
           dataIndex: 'titre',
           key: 'titre',
       },
       {
           title: 'Description',
           dataIndex: 'description',
           key: 'description',
       },
       {
           title: 'Horaire',
           dataIndex: 'horaire',
           key: 'horaire',
           render: (horaire) => moment(horaire).format('YYYY-MM-DD'),
       },
       {
           title: 'Duree',
           dataIndex: 'duree',
           key: 'duree',
       },
       {
           title: 'Entrer',
           key: 'action',
           render: (_, record) => (
               <Button onClick={() => {
                   fetchChatUsers(record);
                   enterChatRoom(record);
               }}>entrer</Button>//
           ),
       },
   ];

    function addChatuser() {
        setInUserAdd(true);
    }

    if (inChatRoom ) {

        // le contenu de la salle de chat
        return (
            <Layout style={{ width: "100%", height: "100%" }}>
                {inUserAdd && (
                    <AddUser currentChat={currentChat} exitAddUser={() => setInUserAdd(false)} />
                )}
                <Header style={{ backgroundColor: "white", textAlign: "center" }}>
                    <h2>Chat Room: {currentChat.titre}</h2>
                </Header>
                <Layout hasSider style={{ height: "100%" }}>
                    <Content style={{ height: "100%" }}>
                        <Input.TextArea id="history" value={history} readOnly style={{ overflowY: "auto", height: "90%", width: "100%" }} />
                        <div style={{ display: "flex", alignItems: "center",height:"10%" }}>
                            <Input value={message} onChange={(e) => setMessage(e.target.value)} style={{ width: '80%', height: '100%', marginRight: '0px' }} />
                            <Button onClick={handleSend} type="primary">envoyer</Button>
                            <Button onClick={exitChatRoom}>EXIT</Button>
                        </div>
                    </Content>
                    <Sider style={{ backgroundColor: "white", display: "flex", flexDirection: "column" }}>
                        <h5 style={{ textAlign: "center" }}>Membre</h5>
                        <div style={{ flex: "1" ,maxHeight:'450px', overflow:'auto'}}>
                            {chatUsers && (
                                <List
                                    style={{ width: "100%", padding: "6px" }}
                                    dataSource={chatUsers}
                                    renderItem={(item) => (
                                        <List.Item key={item.id}>
                                            <List.Item.Meta
                                                title={item.firstName}
                                                description={item.mail}
                                            />
                                        </List.Item>
                                    )}
                                />
                            )}
                        </div>
                        <Button style={{ textAlign: "center", width: "100%" }} onClick={() =>setInUserAdd(true)} type="dashed">
                            <b>+</b>
                        </Button>
                    </Sider>
                </Layout>
            </Layout>
        );
    }
    //le contenu de la page de salons de chat
    if(state==='salons') {
        return (
            <div>
                <Table
                    columns={columns_salons}
                    dataSource={visibleChats}
                    pagination={{
                        current: currentPage,
                        pageSize: pageSize,
                        total: chats.length,
                        onChange: handlePageChange,
                    }}
                />

                {/* Contenu du modal */}
                <Modal
                    title="Modifier le salon"
                    open={inEditRoom}
                    onCancel={() => setInEditRoom(false)}
                    footer={null}
                >
                    <Form onFinish={handleSave}>
                        <Form.Item label="titre" name="titre" rules={[{required: true}]}>
                            <Input value={editedChat?.titre} onChange={handleChange}/>
                        </Form.Item>

                        <Form.Item
                            label="description"
                            name="description"
                            rules={[{required: true}]}
                        >
                            <Input.TextArea
                                value={editedChat?.description}
                                onChange={handleChange}
                            />
                        </Form.Item>

                        <Form.Item label="durée" name="duree" rules={[{required: true}]}>
                            <Input value={editedChat?.duree} onChange={handleChange}/>
                        </Form.Item>

                        <Button type="primary" htmlType="submit">
                            Enregister
                        </Button>
                    </Form>
                </Modal>
            </div>
        );
    //le contenu de la page d'invitation
    }else if(state==='invitations'){

       return(
           <div>
               <Table
                   dataSource={visibleChats}
                   columns={columns_invitation}
                   pagination={{
                       current: currentPage,
                       pageSize: pageSize,
                       total: chats.length,
                       onChange: handlePageChange,
                   }}
               />
           </div>

       );

    }

};

export { ChatListSalons};