package link.youchu.youchuserver.service;

import link.youchu.youchuserver.domain.Banner;
import link.youchu.youchuserver.repository.BannerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BannerService {

    private final BannerRepository repository;

    @Transactional
    public List<Banner> getBanner(){
        return repository.findAll();
    }
}
