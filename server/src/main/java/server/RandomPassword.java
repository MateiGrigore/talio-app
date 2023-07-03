package server;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class RandomPassword {

    /**
     * Method that generates a random String which will be the server password
     * @return a ransom String
     */
    public String generateRandomPassword(){
        Random random = new Random();
        // Random length of the password
        int n = random.nextInt(5, 10);

        String[] characterArray = new String[36];
        for(int i = 0; i < 36; i++){
            if(i < 10){
                characterArray[i] = String.valueOf(i);
            } else {
                char character = (char) ('a' + i - 10);
                characterArray[i] = String.valueOf(character);
            }
        }

        String res = "";
        for(int i = 0; i < n; i++){
            res = res + characterArray[random.nextInt(0, 36)];
        }

        return res;
    }
}
