package guru.qa.niffler.service;

import guru.qa.niffler.model.SpendJson;

public interface SpendClient {
    public SpendJson createSpend(SpendJson spend);
    public void remove(SpendJson spend);

}
