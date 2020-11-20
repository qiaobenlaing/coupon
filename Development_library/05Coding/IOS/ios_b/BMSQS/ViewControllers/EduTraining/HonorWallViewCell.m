//
//  HonorWallViewCell.m
//  BMSQS
//
//  Created by 新利软件－冯 on 16/3/3.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "HonorWallViewCell.h"
#import "UIImageView+WebCache.h"
@implementation HonorWallViewCell

- (void)awakeFromNib {
    // Initialization code
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        [self addSubview:self.HonorImage];
    }
    return self;
}

- (UIImageView *)HonorImage
{
    if (_HonorImage == nil) {
        self.HonorImage = [[UIImageView alloc] initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 180)];
        _HonorImage.backgroundColor = [UIColor greenColor];
    }
    return _HonorImage;
}

- (void)setCellHonorDic:(NSDictionary *)HonorDic number:(int)number
{
    if (number == 1) {
        [self.HonorImage sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@", APP_SERVERCE_IMG_URL,HonorDic[@"workUrl"] ]] placeholderImage:[UIImage imageNamed:@"iv_noShopLog"]];
    }else if (number == 2){
        [self.HonorImage sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@", APP_SERVERCE_IMG_URL,HonorDic[@"starImgUrl"] ]] placeholderImage:[UIImage imageNamed:@"iv_noShopLog"]];
    }
    
}


@end
