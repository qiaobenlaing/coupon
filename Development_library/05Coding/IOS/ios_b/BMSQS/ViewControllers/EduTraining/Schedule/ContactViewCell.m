//
//  ContactViewCell.m
//  BMSQS
//
//  Created by 新利软件－冯 on 16/3/16.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "ContactViewCell.h"

@interface ContactViewCell ()

@property (nonatomic, strong)UIImageView * phoneImage;
@property (nonatomic, strong)UILabel * phoneNumber;
@property (nonatomic, strong)UILabel * conent;

@property (nonatomic, strong)NSString * phoneStr;

@end


@implementation ContactViewCell

- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier {
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if(self) {
        [self setViewUp];
    }
    
    return self;
}

- (void)setViewUp
{
    self.phoneImage = [[UIImageView alloc] initWithFrame:CGRectMake(5, 5, 30, 30)];
    self.phoneImage.backgroundColor = [UIColor greenColor];
    self.phoneImage.image = [UIImage imageNamed:@""];
    self.phoneImage.userInteractionEnabled = YES;
    
    UITapGestureRecognizer*tapGesture = [[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(tapGesturePhone)];
    
    [self.phoneImage addGestureRecognizer:tapGesture];
    

    
    [self addSubview:self.phoneImage];
    
    self.phoneNumber = [[UILabel alloc] initWithFrame:CGRectMake(40, 5, APP_VIEW_WIDTH/2, 30)];
    self.phoneNumber.text = @"18137782363";
    self.phoneNumber.font = [UIFont systemFontOfSize:14.0];
    self.phoneNumber.backgroundColor = [UIColor grayColor];
    [self addSubview:self.phoneNumber];
    
    self.conent = [[UILabel alloc] initWithFrame:CGRectMake(APP_VIEW_WIDTH - 80, 5, 60, 30)];
    self.conent.font = [UIFont systemFontOfSize:12.0];
    self.conent.text = @"免费咨询";
    self.conent.textColor = [UIColor grayColor];
//    self.conent.backgroundColor = [UIColor redColor];
    [self addSubview:self.conent];
    
    
}

- (void)setCellWithConentDic:(NSDictionary *)dic
{
    self.phoneStr = dic[@""];
//    self.phoneNumber.text = [NSString stringWithFormat:@"%@", dic[@""]];
}

- (void)tapGesturePhone
{
    if ([self.contactDelegate respondsToSelector:@selector(setCellViewCellDelegate:)]) {
        [self.contactDelegate setCellViewCellDelegate:self.phoneStr];
    }
}

@end
