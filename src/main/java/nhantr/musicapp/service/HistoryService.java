package nhantr.musicapp.service;

import java.util.UUID;
import nhantr.musicapp.dto.request.HistoryRequest;
import nhantr.musicapp.dto.response.HistoryResponse;
import nhantr.musicapp.dto.response.PageResponse;

public interface HistoryService {

    PageResponse<HistoryResponse> getHistory(int page, int size);

    void add(HistoryRequest request);

    void clearAll();

    void delete(UUID id);
}
