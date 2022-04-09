package org.tp.batch;


import org.springframework.batch.item.ItemProcessor;
import org.tp.transactional.entity.ReadData;
import org.tp.transactional.entity.WriteData;

public class PersonItemProcessor implements ItemProcessor<ReadData, WriteData> {

    @Override
    public WriteData process(ReadData readData) {
        WriteData writeData = new WriteData();
//        writeData.setId(readData.getId());
        writeData.setNo(readData.getMobileNo());
        writeData.setName(readData.getName());
        return writeData;
    }
}
