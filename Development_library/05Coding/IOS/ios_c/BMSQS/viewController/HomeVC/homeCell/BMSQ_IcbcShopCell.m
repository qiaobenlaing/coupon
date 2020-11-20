//
//  BMSQ_IcbcShopCell.m
//  BMSQC
//
//  Created by dongzhonghui on 15/12/4.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "BMSQ_IcbcShopCell.h"
#import "UIImageView+WebCache.h"

@interface BMSQ_IcbcShopCell(){
    
}
@property(nonatomic,strong)UIImageView *shopLogoImg;
@property(nonatomic,strong)UILabel *shopNameLabel;
@property(nonatomic,strong)UIImageView *couponImg;
@property(nonatomic,strong)UIImageView *actImg;
@property(nonatomic,strong)UIImageView *icbcImg;
@property(nonatomic,strong)UILabel *addressLabel;
@property(nonatomic,strong)UILabel *typeLabel;
@property(nonatomic,strong)UILabel *distanceLabel;
@property(nonatomic,strong)UILabel *popularityLabel;
@property(nonatomic,strong)UILabel *discountLabel;
@property(nonatomic,strong)UIView *imageBackView;
@property(nonatomic,strong)UIView *line;
@property(nonatomic,strong)UIView *line0;

@end

@implementation BMSQ_IcbcShopCell
-(id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        [self setViewUp];
    }
    return self;
}

-(void)setViewUp{
    CGFloat cellHeight = APP_VIEW_WIDTH/4;
    CGFloat imageHeight = cellHeight - 20;
    
    self.frame = CGRectMake(0, 0, APP_VIEW_WIDTH, APP_VIEW_WIDTH/4);
    //商家logo
    self.shopLogoImg = [[UIImageView alloc] initWithFrame:CGRectMake(10, 10, imageHeight, imageHeight)];
    [self addSubview:self.shopLogoImg];
    //商家名称
    self.shopNameLabel = [[UILabel alloc] initWithFrame:CGRectMake(self.shopLogoImg.frame.origin.x + imageHeight + 10, 10, APP_VIEW_WIDTH - (self.shopLogoImg.frame.origin.x + imageHeight + 10), cellHeight/4-10)];
    self.shopNameLabel.text = @"商家名称";
    self.shopNameLabel.font = [UIFont boldSystemFontOfSize:14.f];
    [self addSubview:self.shopNameLabel];
    //惠，活
    self.couponImg = [[UIImageView alloc]init];
    self.couponImg.image = [UIImage imageNamed:@"惠"];
    [self addSubview:self.couponImg];
    self.actImg = [[UIImageView alloc]init];
    self.actImg.image = [UIImage imageNamed:@"活"];
    [self addSubview:self.actImg];
    //地址
    self.addressLabel = [[UILabel alloc] initWithFrame:CGRectMake(self.shopLogoImg.frame.origin.x + imageHeight + 10, cellHeight/4 * 2, APP_VIEW_WIDTH, cellHeight/4)];
    self.addressLabel.text = @"地址";
    self.addressLabel.textColor = [UIColor colorWithRed:76/255.0 green:76/255.0 blue:76/255.0 alpha:1];
    self.addressLabel.font = [UIFont systemFontOfSize:11.f];
    [self addSubview:self.addressLabel];
    //商家类型
    self.typeLabel = [[UILabel alloc] initWithFrame:CGRectMake(self.shopLogoImg.frame.origin.x + imageHeight + 10, cellHeight/4 * 3, APP_VIEW_WIDTH, cellHeight/4)];
    self.typeLabel.text = @"商铺类型";
    self.typeLabel.textColor = [UIColor colorWithRed:76/255.0 green:76/255.0 blue:76/255.0 alpha:1];
    self.typeLabel.font = [UIFont systemFontOfSize:11.f];
    [self addSubview:self.typeLabel];
    //工行图标
    self.icbcImg = [[UIImageView alloc] init];
    self.icbcImg.backgroundColor = [UIColor clearColor];
    [self addSubview:self.icbcImg];
    //折扣
    self.discountLabel = [[UILabel alloc] initWithFrame:CGRectMake(APP_VIEW_WIDTH/2, 0, APP_VIEW_WIDTH/2 - 10, cellHeight/2)];
    self.discountLabel.textColor = [UIColor orangeColor];
    self.discountLabel.font = [UIFont systemFontOfSize:18.f];
    self.discountLabel.textAlignment = NSTextAlignmentRight;
    self.discountLabel.text = @"折扣";
    [self addSubview:self.discountLabel];
    
    //距离
    self.distanceLabel = [[UILabel alloc] initWithFrame:CGRectMake(APP_VIEW_WIDTH/2, cellHeight/4 * 2, APP_VIEW_WIDTH/2 - 10, cellHeight/4)];
    self.distanceLabel.backgroundColor = [UIColor clearColor];
    self.distanceLabel.textColor = [UIColor colorWithRed:76/255.0 green:76/255.0 blue:76/255.0 alpha:1];
    self.distanceLabel.font = [UIFont systemFontOfSize:11.f];
    self.distanceLabel.textAlignment = NSTextAlignmentRight;
    self.distanceLabel.text = @"距离";
    [self addSubview:self.distanceLabel];
    //人气
    self.popularityLabel = [[UILabel alloc] initWithFrame:CGRectMake(APP_VIEW_WIDTH/2, cellHeight/4 * 3, APP_VIEW_WIDTH/2 - 10, cellHeight/4)];
    self.popularityLabel.textAlignment = NSTextAlignmentRight;
    self.popularityLabel.textColor = [UIColor colorWithRed:76/255.0 green:76/255.0 blue:76/255.0 alpha:1];
    self.popularityLabel.font = [UIFont systemFontOfSize:11.f];
    self.popularityLabel.text = @"人气";
    [self addSubview:self.popularityLabel];
    //分隔线
    self.line = [[UIView alloc]init];
    self.line.backgroundColor = APP_CELL_LINE_COLOR;
    [self addSubview:self.line];
    self.line0 = [[UIView alloc] init];
    self.line0.backgroundColor = APP_CELL_LINE_COLOR;
    [self addSubview:self.line0];
    
    self.imageBackView = [[UIView alloc] init];
    self.imageBackView.backgroundColor = [UIColor clearColor];
    [self addSubview:self.imageBackView];
}

+(CGFloat)cellHeight:(NSDictionary *)dic{
    CGFloat height = APP_VIEW_WIDTH/4;
    return height;
}

-(void)setCellValue:(NSDictionary *)dictionary{
    [self.shopLogoImg sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@/%@", IMAGE_URL, [dictionary objectForKey:@"logoUrl"]]] placeholderImage:[UIImage imageNamed:@"iv__noShopLog"]];
    NSString *shopNameStr = [NSString stringWithFormat:@"%@",[dictionary objectForKey:@"shopName"]];
    self.shopNameLabel.text = shopNameStr;
    self.addressLabel.text = [NSString stringWithFormat:@"%@",[dictionary objectForKey:@"street"]];
    NSArray *array = [NSArray arrayWithObjects:@"",@"美食",@"丽人",@"健身",@"娱乐",@"服装",@"其它",@"",nil];
    int type = [[dictionary objectForKey:@"type"]intValue];
    self.typeLabel.text = [NSString stringWithFormat:@"%@", array[type]];
    float distance = [[dictionary objectForKey:@"distance"] floatValue]/1000;
    self.distanceLabel.text = distance > 10?[NSString stringWithFormat:@">10km"]:[NSString stringWithFormat:@"%0.2fkm",distance];
    self.popularityLabel.text = [NSString stringWithFormat:@"人气:%@",[dictionary objectForKey:@"popularity"]];
    if ([[dictionary objectForKey:@"hasIcbcDiscount"] intValue] == 1) {
        CGSize shopSize = [shopNameStr sizeWithFont:[UIFont boldSystemFontOfSize:14.f] constrainedToSize:CGSizeMake(MAXFLOAT, 30)];
        self.icbcImg.frame = CGRectMake(self.shopNameLabel.frame.origin.x + shopSize.width + 5, self.shopNameLabel.frame.origin.y, self.shopNameLabel.frame.size.height, self.shopNameLabel.frame.size.height);
        self.icbcImg.image = [UIImage imageNamed:@"ICBCcardicon"];
        self.icbcImg.hidden = NO;
    } else {
        self.icbcImg.hidden = YES;
    }
    
    CGFloat cellHeight = APP_VIEW_WIDTH / 4;
    CGFloat imageHeight = cellHeight - 20;
    if ([[dictionary objectForKey:@"hasCoupon"] intValue] == 1) {
        self.couponImg.hidden = NO;
        self.couponImg.frame = CGRectMake(self.shopLogoImg.frame.origin.x + imageHeight + 10, cellHeight/4 + 5, 15, 15);
        self.couponImg.image = [UIImage imageNamed:@"惠"];
    } else {
        self.couponImg.hidden = YES;
    }
    if ([[dictionary objectForKey:@"hasAct"]intValue] == 1) {
        if (self.couponImg.hidden == YES) {
            self.actImg.frame = CGRectMake(self.shopLogoImg.frame.origin.x + imageHeight + 10, cellHeight/4 + 5, 15, 15);
        } else {
            self.actImg.frame = CGRectMake(self.shopLogoImg.frame.origin.x + imageHeight +10 + cellHeight/4, cellHeight/4 + 5, 15, 15);
        }
        self.actImg.hidden = NO;
        self.actImg.image = [UIImage imageNamed:@"活"];
    } else {
        self.actImg.hidden = YES;
    }
    
    //如果没有活动和优惠调整位置
    if ((self.couponImg.hidden == YES) && (self.actImg.hidden == YES)) {
        self.addressLabel.frame = CGRectMake((self.shopLogoImg.frame.origin.x+imageHeight+10), cellHeight/3, APP_VIEW_WIDTH, cellHeight/4);
        self.typeLabel.frame = CGRectMake((self.shopLogoImg.frame.origin.x+imageHeight+10), cellHeight/3*2, APP_VIEW_WIDTH, cellHeight/4);
        
    }else {
        self.addressLabel.frame = CGRectMake((self.shopLogoImg.frame.origin.x+imageHeight+10), cellHeight/4*2, APP_VIEW_WIDTH, cellHeight/4);
        self.typeLabel.frame = CGRectMake((self.shopLogoImg.frame.origin.x+imageHeight+10), cellHeight/4*3, APP_VIEW_WIDTH, cellHeight/4);
        
    }
    //折扣
    NSString *onlinePaymentDiscount = [NSString stringWithFormat:@"%@",[dictionary objectForKey:@"onlinePaymentDiscount"]];
    self.discountLabel.text = [NSString stringWithFormat:@"%0.1f折",[onlinePaymentDiscount floatValue]];
    
    
    self.imageBackView.hidden = YES;
    self.line0.hidden = YES;
    self.line.frame = CGRectMake(0, APP_VIEW_WIDTH/4-0.5, APP_VIEW_WIDTH, 0.5);
}
@end
