# ChatTCP
perl

# Projeto Lab-Sockets Modificado

Este projeto é uma modificação do projeto original disponível em https://github.com/marcuswac/sd-ufpb/tree/main/labs/lab-sockets. </br>

## Modificações Realizadas

1. **Cliente**: No cliente, a classe `ChatTCPClient` foi modificada para permitir que o usuário envie várias mensagens sem fechar o socket ou o scanner. Agora, o cliente pode continuar enviando mensagens até digitar "bye" para encerrar a conexão.

2. **Servidor**: No servidor, a classe `ClientHandler` foi atualizada para possibilitar a troca contínua de mensagens com o cliente. Agora, o servidor não fecha o socket ou o scanner até receber a mensagem "bye" do cliente ou enviar "bye" para o cliente.