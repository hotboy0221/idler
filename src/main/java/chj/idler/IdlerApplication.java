package chj.idler;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("chj.idler.dao")
public class IdlerApplication {

    public static void main(String[] args) {
        try {
            SpringApplication.run(IdlerApplication.class, args);
        }catch (Exception e){
        e.printStackTrace();
         }
    }

}
