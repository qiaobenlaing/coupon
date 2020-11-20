//
//  ActMemberTableViewCell.m
//  BMSQS
//
//  Created by liuqin on 15/10/27.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "ActMemberTableViewCell.h"

#import "UIImageView+AFNetworking.h"


@interface ActMemberTableViewCell ()

@property (nonatomic, strong)UIImageView *headImage;

@property (nonatomic, strong)UILabel *countLabel;

@property (nonatomic, strong)UILabel *bigLabel;

@property (nonatomic, strong)UILabel *smallLabel;

@property (nonatomic, strong)UILabel *timeLabe;

@end






@implementation ActMemberTableViewCell


-(id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier{
    
    self =[super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
       
        self.backgroundColor = [UIColor clearColor];
        UIView *bgView = [[UIView alloc]initWithFrame:CGRectMake(10, 20, APP_VIEW_WIDTH-20, 100)];
        bgView.backgroundColor = [UIColor whiteColor];
        bgView.layer.cornerRadius = 5;
        bgView.layer.masksToBounds = YES;
        [self.contentView addSubview:bgView];
        
        self.headImage = [[UIImageView alloc]initWithFrame:CGRectMake(10, 10, 80, 80)];
        self.headImage.backgroundColor = [UIColor redColor];
        [bgView addSubview:self.headImage];
        
        self.countLabel = [[UILabel alloc]initWithFrame:CGRectMake(100, 5, APP_VIEW_WIDTH-130, 15)];
        self.countLabel.font = [UIFont systemFontOfSize:12.f];
        self.countLabel.textAlignment = NSTextAlignmentRight;
        self.countLabel.text = @"人数: 100000";
        [bgView addSubview:self.countLabel];
        
        self.bigLabel = [[UILabel alloc]initWithFrame:CGRectMake(100, 25, APP_VIEW_WIDTH-130, 25)];
        self.bigLabel.font = [UIFont systemFontOfSize:13.f];
        self.bigLabel.text = @"大人：男 55555人 女88888人";
        [bgView addSubview:self.bigLabel];
        
        
        self.smallLabel = [[UILabel alloc]initWithFrame:CGRectMake(100, 55, APP_VIEW_WIDTH-130, 25)];
        self.smallLabel.font = [UIFont systemFontOfSize:13.f];
        self.smallLabel.text = @"小孩：男 55555人 女88888人";
        [bgView addSubview:self.smallLabel];
        
        self.timeLabe = [[UILabel alloc]initWithFrame:CGRectMake(90, 75, APP_VIEW_WIDTH-130, 25)];
        self.timeLabe.font = [UIFont systemFontOfSize:12.f];
        self.timeLabe.textAlignment = NSTextAlignmentRight;
        self.timeLabe.text = @"0000-00-00 00:00:00";
        [bgView addSubview:self.timeLabe];

    }
    return self;
    
}
-(void)setActMemeberCell:(NSDictionary *)dic{
    NSLog(@"-->%@",dic);
    
    NSString *urlStr =[NSString stringWithFormat:@"%@%@",APP_SERVERCE_IMG_URL,[dic objectForKey:@"avatarUrl"]];
    [self.headImage setImageWithURL:[NSURL URLWithString:urlStr] placeholderImage:[UIImage imageNamed:@"Login_Icon"]];
    self.countLabel.text = [NSString stringWithFormat:@"人数: %@",[dic objectForKey:@"participantNbr"]];
    self.bigLabel.text = [NSString stringWithFormat:@"大人：男 %@ 人 女 %@ 人",[dic objectForKey:@"adultM"],[dic objectForKey:@"adultF"]];
    self.smallLabel.text = [NSString stringWithFormat:@"小孩：男 %@ 人 女 %@ 人",[dic objectForKey:@"kidM"],[dic objectForKey:@"kidF"]];
    self.timeLabe.text = [dic objectForKey:@"signUpTime"];

    
}


@end
