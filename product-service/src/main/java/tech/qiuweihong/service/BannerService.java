package tech.qiuweihong.service;

import tech.qiuweihong.model.BannerDO;
import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiuweihong.vo.BannerVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Qiu Weihong
 * @since 2023-11-13
 */
public interface BannerService {
    List<BannerVO> listBanner();

}
