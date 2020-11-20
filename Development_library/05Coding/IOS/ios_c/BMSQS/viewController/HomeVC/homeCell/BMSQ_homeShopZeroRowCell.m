//
//  BMSQ_homeShopZeroRowCell.m
//  BMSQC
//
//  Created by liuqin on 16/1/27.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "BMSQ_homeShopZeroRowCell.h"

@interface BMSQ_homeShopZeroRowCell ()

@property(nonatomic,strong)UIImageView *shopLogoImg;
@property(nonatomic,strong)UILabel *shopNameLabel;
@property(nonatomic,strong)UIImageView *couponImg;
@property(nonatomic,strong)UIImageView *actImg;
@property(nonatomic,strong)UIImageView *icbcImg;
@property(nonatomic,strong)UILabel *addressLabel;
@property(nonatomic,strong)UILabel *typeLabel;
@property(nonatomic,strong)UIImageView *isSuspendedImage; //是否停业

@property(nonatomic,strong)UILabel *distanceLabel;
@property(nonatomic,strong)UILabel *popularityLabel;
@property(nonatomic,strong)UIView *imageBackView;
@property(nonatomic,strong)UIView *line;
@property(nonatomic,strong)UIView *line0;


@property(nonatomic,strong)UIView *secView;
@property(nonatomic,strong)UILabel *titleLabel;
@end


@implementation BMSQ_homeShopZeroRowCell

- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier {
    
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        [self setViewUp];
    }
    return self;
    
}

- (void)setViewUp {
    CGFloat cellHeight = APP_VIEW_WIDTH/4;
    CGFloat imageHeight = cellHeight-20;
    self.frame = CGRectMake(0, 0, APP_VIEW_WIDTH, APP_VIEW_WIDTH/4);
    
    self.backgroundColor = [UIColor clearColor];
    
    UIView *bgView = [[UIView alloc]initWithFrame:CGRectMake(0, 10, APP_VIEW_WIDTH, 40)];
    bgView.backgroundColor = [UIColor whiteColor];
    [self addSubview:bgView];

    
    self.titleLabel = [[UILabel alloc]initWithFrame:CGRectMake(15, 0, 200, 40)];
    self.titleLabel.backgroundColor = [UIColor clearColor];
    self.titleLabel.font = [UIFont systemFontOfSize:15.f];
    [bgView addSubview:self.titleLabel];
    
    UILabel *moreLabel = [[UILabel alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH-60, 18, 30, 10)];
    moreLabel.backgroundColor = [UIColor clearColor];
    moreLabel.font = [UIFont systemFontOfSize:13.f];
    moreLabel.text = @"更多";
    [bgView addSubview:moreLabel];
    
    UIImageView *imageView = [[UIImageView alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH-30, 18, 10, 10)];
    [imageView setImage:[UIImage imageNamed:@"right_enter"]];
    [bgView addSubview:imageView];

    
    UITapGestureRecognizer *tapGesture = [[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(tapGesture)];
    [bgView addGestureRecognizer:tapGesture];
    
    
    
    self.secView = [[UIView alloc]init];
    self.secView.backgroundColor = [UIColor whiteColor];
    [self addSubview:self.secView];
    
    
    //商家logo
    self.shopLogoImg = [[UIImageView alloc] initWithFrame:CGRectMake(10, 10, imageHeight, imageHeight)];
//    self.shopLogoImg.contentMode = UIViewContentModeScaleAspectFill;
//    self.shopLogoImg.clipsToBounds = YES;
    [self.secView addSubview:self.shopLogoImg];
    
    //商家名称
    self.shopNameLabel = [[UILabel alloc] initWithFrame:CGRectMake((self.shopLogoImg.frame.origin.x+imageHeight+10), 10, APP_VIEW_WIDTH-(self.shopLogoImg.frame.origin.x+imageHeight+10), cellHeight/4-10)];
    self.shopNameLabel.text = @"商家名称";
    self.shopNameLabel.font = [UIFont boldSystemFontOfSize:14.f];
    [self.secView addSubview:self.shopNameLabel];
    
    //WithFrame:
    self.couponImg = [[UIImageView alloc] init];
    self.couponImg.image = [UIImage imageNamed:@"惠"];
    [self.secView addSubview:self.couponImg];
    
    self.actImg = [[UIImageView alloc] init];
    [self.secView addSubview:self.actImg];
    
    self.addressLabel = [[UILabel alloc] init];
    self.addressLabel.text = @"地址";
    self.addressLabel.textColor =[UIColor colorWithRed:76/255.0 green:76/255.0 blue:76/255.0 alpha:1];
    self.addressLabel.font =[UIFont systemFontOfSize:11.f];
    self.addressLabel.backgroundColor = [UIColor clearColor];
    [self.secView addSubview:self.addressLabel];
    
    
    self.typeLabel = [[UILabel alloc] initWithFrame:CGRectMake((self.shopLogoImg.frame.origin.x+imageHeight+10), cellHeight/4*3, APP_VIEW_WIDTH, cellHeight/4)];
    self.typeLabel.text = @"商铺类型";
    self.typeLabel.textColor = [UIColor colorWithRed:76/255.0 green:76/255.0 blue:76/255.0 alpha:1];
    self.typeLabel.font = [UIFont systemFontOfSize:11.f];
    [self.secView addSubview:self.typeLabel];
    
    self.isSuspendedImage = [[UIImageView alloc] initWithFrame:CGRectMake(APP_VIEW_WIDTH-50, 0, 40, 30)];//
    [self.isSuspendedImage setImage:[UIImage imageNamed:@"resting"]];
    [self.secView addSubview:self.isSuspendedImage];
    
    self.icbcImg = [[UIImageView alloc] init];
    self.icbcImg.backgroundColor = [UIColor clearColor];
    [self.secView addSubview:self.icbcImg];
    
    self.distanceLabel = [[UILabel alloc] initWithFrame:CGRectMake(APP_VIEW_WIDTH/2, cellHeight/4*2, APP_VIEW_WIDTH/2-10, cellHeight/4)];
    self.distanceLabel.backgroundColor = [UIColor clearColor];
    self.distanceLabel.textColor = [UIColor colorWithRed:76/255.0 green:76/255.0 blue:76/255.0 alpha:1];
    self.distanceLabel.font = [UIFont systemFontOfSize:11.f];
    self.distanceLabel.textAlignment = NSTextAlignmentRight;
    self.distanceLabel.text = @"距离";
    [self.secView addSubview:self.distanceLabel];
    
    self.popularityLabel = [[UILabel alloc] initWithFrame:CGRectMake(APP_VIEW_WIDTH/2, cellHeight/4*3, APP_VIEW_WIDTH/2-10, cellHeight/4)];
    self.popularityLabel.textAlignment = NSTextAlignmentRight;
    self.popularityLabel.textColor = [UIColor colorWithRed:76/255.0 green:76/255.0 blue:76/255.0 alpha:1];
    self.popularityLabel.font = [UIFont systemFontOfSize:11.f];
    self.popularityLabel.text = @"人气:";
    [self.secView addSubview:self.popularityLabel];
    
    
    self.line = [[UIView alloc ] init];
    self.line.backgroundColor = APP_CELL_LINE_COLOR;
    [self.secView addSubview:self.line];
    
    self.line0 = [[UIView alloc ] init];
    self.line0.backgroundColor = APP_CELL_LINE_COLOR;
    [self.secView addSubview:self.line0];
    
    
    self.imageBackView  = [[UIView alloc] init];
    self.imageBackView.backgroundColor = [UIColor clearColor];
    [self.secView addSubview:self.imageBackView];
    
    
}

- (void)setCellValue:(NSDictionary *)dictionary titleDic:(NSDictionary *)titleDic{
    
    //isSuspended，是否暂停营业，1表示是，0表示否。
    
    NSString *isSuspended =[NSString stringWithFormat:@"%@",[dictionary objectForKey:@"isSuspended"]];
    if ([isSuspended isEqualToString:@"1"]) {
        self.isSuspendedImage.hidden = NO;
    }else{
        self.isSuspendedImage.hidden = YES;
    }
    
    
    
    self.titleLabel.text = [titleDic objectForKey:@"moduleTitle"];

    [self.shopLogoImg sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@/%@",IMAGE_URL,[dictionary objectForKey:@"logoUrl"]]] placeholderImage:[UIImage imageNamed:@"iv__noShopLog"]];
    
    NSString *shopNameStr = [NSString stringWithFormat:@"%@",[dictionary objectForKey:@"shopName"]];
    self.shopNameLabel.text = shopNameStr;
    self.addressLabel.text = [NSString stringWithFormat:@"%@",[dictionary objectForKey:@"street"]];
    
    NSArray *array = [NSArray arrayWithObjects:@"",@"美食", @"丽人", @"健身", @"娱乐", @"服装", @"其他", @"", nil];
    int type = [[dictionary objectForKey:@"type"] intValue];
    self.typeLabel.text = [NSString stringWithFormat:@"%@",array[type]];
    
    CGSize size = [self.typeLabel.text boundingRectWithSize:CGSizeMake(40, 40)
                                                    options:NSStringDrawingUsesLineFragmentOrigin
                                                 attributes:@{NSFontAttributeName:self.typeLabel.font}
                                                    context:nil].size;
    
    
    
    self.popularityLabel.text = [NSString stringWithFormat:@"人气:%@",[dictionary objectForKey:@"popularity"]];
    
    float distance = [[dictionary objectForKey:@"distance"]floatValue]/1000;
    self.distanceLabel.text =distance>1000? [NSString stringWithFormat:@">1000km"]:[NSString stringWithFormat:@"%0.2fkm",distance];
    
    if ([[dictionary objectForKey:@"hasIcbcDiscount"] intValue]==1) {
        CGSize shopSize= [shopNameStr boundingRectWithSize:CGSizeMake(MAXFLOAT, 30)
                                                   options:NSStringDrawingUsesLineFragmentOrigin
                                                attributes:@{NSFontAttributeName:[UIFont systemFontOfSize:14.f]}
                                                   context:nil].size;
        
        self.icbcImg.frame = CGRectMake(self.shopNameLabel.frame.origin.x + shopSize.width + 5, self.shopNameLabel.frame.origin.y, self.shopNameLabel.frame.size.height, self.shopNameLabel.frame.size.height);
        self.icbcImg.image = [UIImage imageNamed:@"ICBCcardicon"];
        self.icbcImg.hidden = NO;
    }else {
        self.icbcImg.hidden = YES;
    }
    
    CGFloat cellHeight = APP_VIEW_WIDTH/4;
    CGFloat imageHeight = cellHeight-20;
    
    //惠
    if ([[dictionary objectForKey:@"hasCoupon"]intValue] == 1 ) {
        self.couponImg.hidden = NO;
        self.couponImg.frame = CGRectMake((self.shopLogoImg.frame.origin.x+imageHeight+10), cellHeight/4+5, 15, 15);
        self.couponImg.image = [UIImage imageNamed:@"惠"];
        
    }else {
        self.couponImg.hidden = YES;
    }
    
    //活
    if ([[dictionary objectForKey:@"hasAct"] intValue]==1) {
        if (self.couponImg.hidden == YES) {
            self.actImg.frame = CGRectMake((self.shopLogoImg.frame.origin.x+imageHeight+10), cellHeight/4+5, 15, 15);
        }else {
            self.actImg.frame = CGRectMake((self.shopLogoImg.frame.origin.x+imageHeight+10+cellHeight/4), cellHeight/4+5, 15, 15);
        }
        
        self.actImg.hidden = NO;
        self.actImg.image = [UIImage imageNamed:@"活"];
        
    }else {
        self.actImg.hidden = YES;
        
    }
    
    //如果没有活动和优惠调整位置
    if ((self.couponImg.hidden == YES) && (self.actImg.hidden == YES)) {
        self.addressLabel.frame = CGRectMake((self.shopLogoImg.frame.origin.x+imageHeight+10), cellHeight/3,APP_VIEW_WIDTH-(self.shopLogoImg.frame.origin.x+imageHeight+10)-20, cellHeight/4);
        self.typeLabel.frame = CGRectMake((self.shopLogoImg.frame.origin.x+imageHeight+10), cellHeight/3*2, size.width, cellHeight/4);
       
    }else {
        self.addressLabel.frame = CGRectMake((self.shopLogoImg.frame.origin.x+imageHeight+10), cellHeight/4*2,  APP_VIEW_WIDTH-(self.shopLogoImg.frame.origin.x+imageHeight+10)-20, cellHeight/4);
        self.typeLabel.frame = CGRectMake((self.shopLogoImg.frame.origin.x+imageHeight+10), cellHeight/4*3,  size.width, cellHeight/4);
        
    }
    
    //是否有新品
    if ([[dictionary objectForKey:@"hasNewProduct"] intValue] == 1) {
        NSArray *imageArray = [dictionary objectForKey:@"newProductList"];
        self.imageBackView.hidden = NO;
        self.line0.hidden = NO;
        self.line0.frame = CGRectMake(10, APP_VIEW_WIDTH/4-0.5, APP_VIEW_WIDTH, 0.5);
        
        self.imageBackView.frame = CGRectMake(15, self.line0.frame.origin.y, APP_VIEW_WIDTH-30, APP_VIEW_WIDTH/4) ;
        float w = self.imageBackView.frame.size.height-20;
        int sp = (self.imageBackView.frame.size.width-w*4)/3;
        
        for (int i = 0;i<imageArray.count;i++) {
            NSDictionary *imageDic = [imageArray objectAtIndex:i];
            UIImageView *photoImageView = [[UIImageView alloc]initWithFrame:CGRectMake(i*(w+sp), 10, w, w)];
            NSString *str = [NSString stringWithFormat:@"%@%@",IMAGE_URL,[imageDic objectForKey:@"productImg"]];
            [photoImageView sd_setImageWithURL:[NSURL URLWithString:str] placeholderImage:[UIImage imageNamed:@"iv__noShopLog"]];
            [self.imageBackView addSubview:photoImageView];
            
            UIImageView *samll = [[UIImageView alloc]initWithFrame:CGRectMake(0, 0, 30, 30)];
            [samll setImage:[UIImage imageNamed:@"smallNew"]];
            [photoImageView addSubview:samll];
            
        }
        
        self.line.frame = CGRectMake(0, APP_VIEW_WIDTH/2-0.5, APP_VIEW_WIDTH, 0.5);
    }else {
        self.imageBackView.hidden = YES;
        self.line0.hidden = YES;
        self.line.frame = CGRectMake(0, APP_VIEW_WIDTH/4-0.5, APP_VIEW_WIDTH, 0.5);
    }
    
    
    CGFloat heigh =  APP_VIEW_WIDTH/4;
    
    NSArray *newPhotoList = [dictionary objectForKey:@"newProductList"];
    
    if (newPhotoList.count >0) {
        
        heigh = heigh + APP_VIEW_WIDTH/4;
    }
    
    self.secView.frame = CGRectMake(0, 50, APP_VIEW_WIDTH, heigh);
    
}

+(CGFloat )cellHeigh:(NSDictionary *)dic{
    
    CGFloat heigh =  APP_VIEW_WIDTH/4;
    
    NSArray *newPhotoList = [dic objectForKey:@"newProductList"];
    
    if (newPhotoList.count >0) {
        
        heigh = heigh + APP_VIEW_WIDTH/4;
    }
    
    return heigh+50;
}

-(void)tapGesture{
    if ([self.zeroDelegate respondsToSelector:@selector(gotoShopVC)]) {
        [self.zeroDelegate gotoShopVC];
    }
    
}


@end
