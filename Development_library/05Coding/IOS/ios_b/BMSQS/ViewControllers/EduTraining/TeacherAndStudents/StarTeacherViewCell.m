//
//  StarTeacherViewCell.m
//  BMSQS
//
//  Created by 新利软件－冯 on 16/3/3.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "StarTeacherViewCell.h"
#import "UIImageView+WebCache.h"
@implementation StarTeacherViewCell

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
        [self addSubview:self.iconImage];
        [self addSubview:self.nameLB];
        [self addSubview:self.subjectLB];//
        [self addSubview:self.levelLB];
        [self addSubview:self.introductionLB];
        
        }
    return self;
}

- (UIImageView *)iconImage
{
    if (_iconImage == nil) {
        self.iconImage = [[UIImageView alloc] initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH/5*3, APP_VIEW_WIDTH/5*3)];
        _iconImage.image = [UIImage imageNamed:@"share"];
    }
    return _iconImage;
}

- (UILabel *)nameLB
{
    if (_nameLB == nil) {
        self.nameLB = [[UILabel alloc] initWithFrame:CGRectMake(APP_VIEW_WIDTH/5*3 + 5, 10, APP_VIEW_WIDTH/5*2 - 60, 20)];
        _nameLB.font = [UIFont systemFontOfSize:14.0];
        _nameLB.backgroundColor = [UIColor redColor];
    }
    return _nameLB;
}

- (UILabel *)subjectLB
{
    if (_subjectLB == nil) {
        self.subjectLB = [[UILabel alloc] initWithFrame:CGRectMake(APP_VIEW_WIDTH - 50, 10, 50, 20)];
        _subjectLB.font = [UIFont systemFontOfSize:14.0];
        _subjectLB.backgroundColor = [UIColor purpleColor];
    }
    return _subjectLB;
}

- (UILabel *)levelLB
{
    if (_levelLB == nil) {
        self.levelLB = [[UILabel alloc] initWithFrame:CGRectMake(APP_VIEW_WIDTH/5*3 + 5, 40, APP_VIEW_WIDTH/5*2 - 10, 20)];
        _levelLB.font = [UIFont systemFontOfSize:14.0];
        _levelLB.backgroundColor = [UIColor greenColor];
    }
    return _levelLB;
}

- (UILabel *)introductionLB
{
    if (_introductionLB == nil) {
        self.introductionLB = [[UILabel alloc] initWithFrame:CGRectMake(APP_VIEW_WIDTH/5*3+5, 70, APP_VIEW_WIDTH/5*2 - 10, 40)];//APP_VIEW_WIDTH/5*3 - 70
        _introductionLB.font = [UIFont systemFontOfSize:12.0];
        _introductionLB.numberOfLines = 0;
        _introductionLB.backgroundColor = [UIColor yellowColor];
    }
    return _introductionLB;
}

- (void)setCellValueDic:(NSDictionary *)valueDic
{
    [self.iconImage sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@", APP_SERVERCE_IMG_URL,valueDic[@"teacherImgUrl"] ]] placeholderImage:[UIImage imageNamed:@"iv_noShopLog"]];
    self.nameLB.text = [NSString stringWithFormat:@"%@", valueDic[@"teacherName"]];
    self.subjectLB.text = [NSString stringWithFormat:@"%@", valueDic[@"teachCourse"]];
    self.levelLB.text = [NSString stringWithFormat:@"%@", valueDic[@"teacherTitle"]];
    self.introductionLB.text = [NSString stringWithFormat:@"简介:%@", valueDic[@"teacherInfo"]];
    CGSize size = [self.introductionLB.text boundingRectWithSize:CGSizeMake(self.introductionLB.frame.size.width, MAXFLOAT) options:NSStringDrawingTruncatesLastVisibleLine | NSStringDrawingUsesFontLeading | NSStringDrawingUsesLineFragmentOrigin attributes:@{NSFontAttributeName: self.introductionLB.font} context:nil].size;
    self.introductionLB.frame = CGRectMake(APP_VIEW_WIDTH/5*3+5, 70, APP_VIEW_WIDTH/5*2 - 10, size.height);
    if (size.height > APP_VIEW_WIDTH/5*3 - 70) {
        self.iconImage.frame = CGRectMake(0, 0, APP_VIEW_WIDTH/5*3, size.height + 70);
    }
    
}


@end
