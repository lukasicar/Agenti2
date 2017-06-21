# Agenti2




.\standalone.bat -Djboss.server.default.config=standalone-full.xml -Dlocal="127.0.0.1:8080" -Dalias=MasterNode 

# Slave Node 1
.\standalone.bat -Djboss.server.default.config=standalone-full.xml -Dlocal="127.0.0.1:8180" -Dmaster="127.0.0.1:8080" -Dalias=SlaveNode1 -Djboss.socket.binding.port-offset=100 



# Slave Node 2
.\standalone.bat -Djboss.server.default.config=standalone-full.xml -Dlocal="127.0.0.1:8280" -Dmaster="127.0.0.1:8080" -Dalias=SlaveNode2 -Djboss.socket.binding.port-offset=200 