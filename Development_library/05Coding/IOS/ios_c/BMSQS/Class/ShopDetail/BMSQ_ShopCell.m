//
//  BMSQ_ShopCell.m
//  BMSQC
//
//  Created by djx on 15/7/29.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "BMSQ_ShopCell.h"
#import "UIImageView+WebCache.h"


@interface BMSQ_ShopCell ()


@property (nonatomic, strong)UIView *BackView;
@property (nonatomic, strong)UIView *topline;

@property (nonatomic, strong)UIImageView *headImageView;
@property (nonatomic, strong)UILabel *nameLabel;
@property (nonatomic, strong)UILabel *addressLabel;

@property (nonatomic, strong)UILabel *distanceLabe;
@property (nonatomic, strong)UIImageView *popularityImageView;
@property (nonatomic, strong)UILabel *popularityLabel;
@property (nonatomic, strong)UIView *secLine;

@property (nonatomic, strong)UILabel *typeLabel;

@property (nonatomic, strong)UIButton *gotoButton;

@property (nonatomic, strong)UIView *bottomView;


@end

@implementation BMSQ_ShopCell

- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self)
    {
        self.backgroundColor = [UIColor colorWithRed:235.0/255.0 green:233.0/255.0 blue:241.0/255.0 alpha:1];
        
        self.BackView = [[UIView alloc]initWithFrame:CGRectMake(0, 10, APP_VIEW_WIDTH, 0)];
        self.BackView.backgroundColor = [UIColor whiteColor];
        [self.contentView addSubview:self.BackView];
        
        
        self.topline = [[UIView alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, 0.4)];
        self.topline.backgroundColor = [UIColor grayColor];
        self.topline.alpha= 0.5;
        [self.BackView addSubview:self.topline];
        
        self.headImageView = [[UIImageView alloc]init];
        self.headImageView.backgroundColor = [UIColor clearColor];
        self.headImageView.layer.masksToBounds = YES;
        self.headImageView.layer.cornerRadius = 3;
        [self.BackView addSubview:self.headImageView];
        
        self.nameLabel = [[UILabel alloc]init];
        self.nameLabel.backgroundColor = [UIColor clearColor];
        self.nameLabel.font = [UIFont boldSystemFontOfSize:14.f];
        [self.BackView addSubview:self.nameLabel];
        
        self.addressLabel = [[UILabel alloc]init];
        self.addressLabel.backgroundColor = [UIColor clearColor];
        self.addressLabel.textColor =[UIColor colorWithRed:76/255.0 green:76/255.0 blue:76/255.0 alpha:1];
        self.addressLabel.font =[UIFont systemFontOfSize:12.f];
        self.addressLabel.numberOfLines = 0;
        [self.BackView addSubview:self.addressLabel];
        
        self.distanceLabe = [[UILabel alloc]init];
        self.distanceLabe.backgroundColor = [UIColor clearColor];
        self.distanceLabe.textColor = [UIColor colorWithRed:76/255.0 green:76/255.0 blue:76/255.0 alpha:1];
        self.distanceLabe.font = [UIFont systemFontOfSize:11.f];
        self.distanceLabe.textAlignment = NSTextAlignmentRight;
        [self.BackView addSubview:self.distanceLabe];
        
        self.popularityImageView = [[UIImageView alloc]init];
        self.popularityImageView.backgroundColor = [UIColor clearColor];
        [self.popularityImageView setImage:[UIImage imageNamed:@"cup"]];
        
        [self.BackView addSubview:self.popularityImageView];
        
        self.popularityLabel = [[UILabel alloc]init];
        self.popularityLabel.backgroundColor = [UIColor clearColor];
        self.popularityLabel.textColor = [UIColor colorWithRed:76/255.0 green:76/255.0 blue:76/255.0 alpha:1];
        self.popularityLabel.font = [UIFont systemFontOfSize:11.f];
        
        [self.BackView addSubview:self.popularityLabel];
        
        
        self.typeLabel = [[UILabel alloc]init];
        self.typeLabel.backgroundColor = [UIColor clearColor];
        self.typeLabel.textColor = [UIColor colorWithRed:76/255.0 green:76/255.0 blue:76/255.0 alpha:1];
        self.typeLabel.font = [UIFont systemFontOfSize:11.f];
        [self.BackView addSubview:self.typeLabel];

        
        
        self.secLine = [[UIView alloc]initWithFrame:CGRectMake(0, 60, APP_VIEW_WIDTH, 0.4)];
        self.secLine.backgroundColor = [UIColor grayColor];
        self.secLine.alpha= 0.5;
        [self.BackView addSubview:self.secLine];
        
        
        self.gotoButton = [[UIButton alloc]init];
        [self.gotoButton setTitle:@"进入店铺" forState:UIControlStateNormal];
        self.gotoButton.layer.borderColor = [[UIColor colorWithRed:250/255.0 green:78/255.0 blue:0/255.0 alpha:1]CGColor];
        [self.gotoButton setTitleColor:[UIColor colorWithRed:250/255.0 green:78/255.0 blue:0/255.0 alpha:1] forState:UIControlStateNormal];
        self.gotoButton.layer.cornerRadius = 3;
        self.gotoButton.layer.masksToBounds = YES;
        self.gotoButton.titleLabel.font = [UIFont systemFontOfSize:13.f];
        self.gotoButton.layer.borderWidth = 0.5;
        [self.gotoButton addTarget:self action:@selector(gotoVC) forControlEvents:UIControlEventTouchUpInside];
        [self.BackView addSubview:self.gotoButton];
        
        self.bottomView = [[UIView alloc]init];
        self.bottomView.backgroundColor = [UIColor whiteColor];
        [self.BackView addSubview:self.bottomView];


    
    }
    
    return self;
}



- (void)setCellValue:(NSDictionary*)dic
{
    
    self.DetailDic = dic;
    
    NSString *urlStr = [[NSString alloc]initWithFormat:@"%@%@",IMAGE_URL,[dic objectForKey:@"logoUrl"]];
    [self.headImageView sd_setImageWithURL:[NSURL URLWithString:urlStr] placeholderImage:[UIImage imageNamed:@"iv_loadingLogo"]];
    self.nameLabel.text = [dic objectForKey:@"shopName"];
    self.addressLabel.text = [dic objectForKey:@"street"];
    float distance = [[dic objectForKey:@"distance"]floatValue]/1000;
    self.distanceLabe.text =distance>10? [NSString stringWithFormat:@">10km"]:[NSString stringWithFormat:@"%0.2fkm",distance];
    self.popularityLabel.text = [NSString stringWithFormat:@"人气:%@",[dic objectForKey:@"popularity"]];
//    self.typeLabel.text = [];
    int type = [[dic objectForKey:@"type"]intValue];
    switch (type) {
        case 0:
        {
            self.typeLabel.text = @"";
        }
            break;
        case 1:
        {
            self.typeLabel.text = @"美食";
        }
            break;
        case 2:
        {
            self.typeLabel.text = @"丽人";
        }
            break;
        case 3:
        {
            self.typeLabel.text = @"健身";
        }
            break;
        case 4:
        {
            self.typeLabel.text = @"娱乐";
        }
            break;
        case 5:
        {
            self.typeLabel.text = @"服装";
        }
            break;
        case 6:
        {
            self.typeLabel.text = @"其他";
        }
            break;
            
        default:
            break;
    }
    
    
    self.headImageView.frame = CGRectMake(15, 10, APP_VIEW_WIDTH/5-10, APP_VIEW_WIDTH/5-10);
    self.nameLabel.frame = CGRectMake(self.headImageView.frame.origin.x+self.headImageView.frame.size.width +10, self.headImageView.frame.origin.y, APP_VIEW_WIDTH-self.headImageView.frame.size.width-15-80, 20) ;
    
    self.distanceLabe.frame = CGRectMake(APP_VIEW_WIDTH-100, 10, 80, 20);
    self.addressLabel.frame = CGRectMake(self.nameLabel.frame.origin.x, 25, APP_VIEW_WIDTH-self.headImageView.frame.origin.x-self.headImageView.frame.size.width-10, self.headImageView.frame.size.height-25);
    
    self.secLine.frame = CGRectMake(0, self.headImageView.frame.origin.y+self.headImageView.frame.size.height+10, APP_VIEW_WIDTH, 0.4);
    
    CGSize size= [self.popularityLabel.text boundingRectWithSize:CGSizeMake(self.distanceLabe.frame.size.width, self.distanceLabe.frame.size.height)
                                                         options:NSStringDrawingUsesLineFragmentOrigin
                                                      attributes:@{NSFontAttributeName:[UIFont systemFontOfSize:11.f]}
                                                         context:nil].size;
    self.popularityLabel.frame = CGRectMake(APP_VIEW_WIDTH-size.width-10, self.secLine.frame.origin.y-20, size.width, size.height);
    self.popularityImageView.frame = CGRectMake(self.popularityLabel.frame.origin.x-20, self.popularityLabel.frame.origin.y,15, self.popularityLabel.frame.size.height);
    self.typeLabel.frame = CGRectMake(10+self.headImageView.frame.origin.x+self.headImageView.frame.size.width, self.secLine.frame.origin.y-20, 30, 10);

    
    self.gotoButton.frame = CGRectMake(APP_VIEW_WIDTH-80, self.secLine.frame.origin.y+5, 70, 25);
    
    
    
    self.bottomView.frame = CGRectMake(0, self.gotoButton.frame.origin.y, APP_VIEW_WIDTH-80, 25);
    
    for (UIView *small in self.bottomView.subviews) {
        [small removeFromSuperview];
    }

    
    int x = 0;
    if ([[dic objectForKey:@"hasCoupon"]integerValue]==1) {
        UILabel *label = [[UILabel alloc]initWithFrame:CGRectMake(x+15, 0, 50, 25)];
       label.text = @"优惠";
        label.backgroundColor = [UIColor colorWithRed:120/255.0 green:187/255.0 blue:252/255.0 alpha:1];
        label.layer.cornerRadius = 3;
        label.layer.masksToBounds = YES;
        x = x+15+50;
        label.textColor = [UIColor whiteColor];
        label.font = [UIFont systemFontOfSize:13.f];
        label.textAlignment = NSTextAlignmentCenter;
        [self.bottomView addSubview:label];
    }
    if ([[dic objectForKey:@"hasAct"]integerValue]==1) {
        UILabel *label = [[UILabel alloc]initWithFrame:CGRectMake(x+15, 0, 50, 25)];
        label.text = @"活动";
        label.backgroundColor = [UIColor colorWithRed:237/255.0 green:167/255.0 blue:83/255.0 alpha:1];
        label.layer.cornerRadius = 3;
        label.layer.masksToBounds = YES;
        label.font = [UIFont systemFontOfSize:13.f];

        label.textColor = [UIColor whiteColor];
        label.textAlignment = NSTextAlignmentCenter;

        x = x+15+50;
        [self.bottomView addSubview:label];
    }
    if ([[dic objectForKey:@"hasNew"]integerValue]==1) {
        UILabel *label = [[UILabel alloc]initWithFrame:CGRectMake(x+15, 0, 50, 25)];
        label.text = @"上新";
        label.backgroundColor = [UIColor colorWithRed:108/255.0 green:206/255.0 blue:137/255.0 alpha:1];
        label.layer.cornerRadius = 3;
        label.layer.masksToBounds = YES;
        label.font = [UIFont systemFontOfSize:13.f];

        label.textColor = [UIColor whiteColor];
        label.textAlignment = NSTextAlignmentCenter;

        [self.bottomView addSubview:label];

    }
    
    
    self.BackView.frame = CGRectMake(0, 10, APP_VIEW_WIDTH, self.bottomView.frame.origin.y+self.bottomView.frame.size.height +5);

}
+(float)sethight:(NSDictionary *)dic{
 
    CGFloat heigh =10+(APP_VIEW_WIDTH/5-10)+10+40;
    
    
    return heigh;
    
}
-(void)gotoVC{
    [self.shopCellDelegate gotoDetail:self.DetailDic];
}
@end
