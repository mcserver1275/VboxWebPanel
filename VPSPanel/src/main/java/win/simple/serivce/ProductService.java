package win.simple.serivce;

import win.simple.entity.ExampleEntity;
import win.simple.entity.StateEntity;

public interface ProductService {

    public StateEntity buyvm(String token, int paymod, int exampleId, int buydate);

    public StateEntity exampleList();

    public StateEntity myResources(String token);

    public StateEntity vmInfo(String token, String vmUUID);

    public StateEntity renew(String token, String vmUUID, int paymod, int buydate);

    public StateEntity resetExample(String token, ExampleEntity exampleEntity);

}
