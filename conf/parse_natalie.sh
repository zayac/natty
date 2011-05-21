#/!/bin/sh
mount -t cifs //natalie.campus/music/\!All /mnt -o iocharset=utf8,file_mode=0777,dir_mode=0777
mvn exec:java -Dexec.mainClass="ru.natty.parser.MainClass"
