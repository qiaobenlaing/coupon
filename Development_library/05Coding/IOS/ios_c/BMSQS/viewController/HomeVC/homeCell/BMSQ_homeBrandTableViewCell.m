//
//  BMSQ_homeBrandTableViewCell.m
//  BMSQC
//
//  Created by liuqin on 15/12/4.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "BMSQ_homeBrandTableViewCell.h"

#import "UIColor+extend.h"
@implementation HomeBrandView
-(id)initWithFrame:(CGRect)frame{
    self = [super initWithFrame:frame];
    if (self) {
//        float w = frame.size.width/3-10;
        self.backgroundColor = [UIColor whiteColor];
        self.bgImage = [[UIImageView alloc]init];
        self.bgImage.backgroundColor = [UIColor clearColor];
        [self addSubview:self.bgImage];
        
        
        UITapGestureRecognizer *tap = [[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(clickActImage)];
        [self addGestureRecognizer:tap];
        
    /*

        self.titleLabel = [[UILabel alloc]initWithFrame:CGRectMake(8, (frame.size.height-w/2)/2, APP_VIEW_WIDTH/2-w-5-8, w/2)];
        self.titleLabel.backgroundColor = [UIColor clearColor];
        self.titleLabel.font = [UIFont boldSystemFontOfSize:12];
        [self addSubview:self.titleLabel];
        

        
        self.contentLabel = [[UILabel alloc]initWithFrame:CGRectMake(self.titleLabel.frame.origin.x, self.titleLabel.frame.origin.y+self.titleLabel.frame.size.height,self.titleLabel.frame.size.width , w/2)];
        self.contentLabel.backgroundColor = [UIColor clearColor];
        self.contentLabel.font = [UIFont systemFontOfSize:10];
        self.contentLabel.numberOfLines = 0;
        [self addSubview:self.contentLabel];
        
        
        self.shopName = [[UILabel alloc]initWithFrame:CGRectMake(8, 8, APP_VIEW_WIDTH/2-w-5-8, w)];
        self.shopName.backgroundColor = [UIColor clearColor];
        self.shopName.numberOfLines = 2;
        self.shopName.textAlignment = NSTextAlignmentCenter;
        self.shopName.font = [UIFont systemFontOfSize:12];
        [self addSubview:self.shopName];
     */
        
    }
    return self;
}

-(void)setIconImageStr:(NSString *)iconStr{
    [self.bgImage sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@",IMAGE_URL,iconStr]] placeholderImage:[UIImage imageNamed:@"iv__noShopLog"]];
    self.bgImage.frame = self.bounds;
    
}

-(void)clickActImage{
    if ([self.delegate respondsToSelector:@selector(HomeBrandViewDelegate:)]) {
        [self.delegate HomeBrandViewDelegate:self.myDic];
    }
    
}

/*
-(void)setHomeBrand:(NSString *)title icon:(NSString *)iconStr connent:(NSString *)conStr isActivity:(BOOL)b{
 
    if (b) {  //活动
        self.titleLabel.hidden = NO;
        self.contentLabel.hidden = NO;
        self.shopName.hidden = YES;
        
    }else{  //圈广场
        
        self.titleLabel.hidden = YES;
        self.contentLabel.hidden = YES;
        self.shopName.hidden = NO;
        self.backgroundColor = [UIColor whiteColor];
    }
    
    [self.iconImage sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@",IMAGE_URL,iconStr]] placeholderImage:[UIImage imageNamed:@"iv__noShopLog"]];
    self.shopName.text = title;
    self.titleLabel.text = title;
    self.contentLabel.text = conStr;
 
    
//    self.titleLabel.hidden = YES;
//    self.contentLabel.hidden = YES;
//    self.shopName.hidden = YES;
    self.iconImage.frame = self.bounds;
    [self.iconImage sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@",IMAGE_URL,iconStr]] placeholderImage:[UIImage imageNamed:@"iv__noShopLog"]];

}
*/
/*
#pragma mark 一个
//-(void)onlyFrame:(NSString *)iconStr{
//    self.backgroundColor = [UIColor whiteColor];
//    self.titleLabel.hidden = YES;
//    self.contentLabel.hidden = YES;
//    self.shopName.hidden = YES;
//    float w = (self.frame.size.width/2)/3;
//    [self.iconImage sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@",IMAGE_URL,iconStr]] placeholderImage:[UIImage imageNamed:@"iv__noShopLog"]];
//    self.iconImage.frame = self.bounds;//CGRectMake(8, 5, APP_VIEW_WIDTH-16, self.frame.size.height-10) ;

//}


//-(void)setAcityBrandFrame:(NSDictionary *)dic{
//    self.titleLabel.hidden = YES;
//    self.contentLabel.hidden = YES;
//    self.shopName.hidden = YES;
//     self.iconImage.frame =self.bounds;//CGRectMake(8,5 , self.frame.size.width-16, self.frame.size.height-10);

    
    NSString *imgPosition;
    if ([dic objectForKey:@"bgColor"]) {
        NSString *bgColor = [dic objectForKey:@"bgColor"];
        self.backgroundColor = [UIColor colorFromHexRGB:bgColor];
        self.shopName.hidden = YES;
        self.titleLabel.hidden = NO;
        self.contentLabel.hidden = NO;
        
        NSString *titleColor = [dic objectForKey:@"titleColor"];
        self.titleLabel.textColor = [UIColor colorFromHexRGB:titleColor];
        NSString *subTitleColor = [dic objectForKey:@"subTitleColor"];
        self.contentLabel.textColor = [UIColor colorFromHexRGB:subTitleColor];
       imgPosition = [NSString stringWithFormat:@"%@",[dic objectForKey:@"imgPosition"]];
    }else{
        
        self.shopName.hidden = NO;
        self.titleLabel.hidden = YES;
        self.contentLabel.hidden = YES;
        imgPosition = @"2";

    }
   
    float w = self.frame.size.width/3;
    switch ([imgPosition intValue]) {
        case 0:  //左
        {
            self.iconImage.frame =CGRectMake(15,self.frame.size.height/2-w/2 , w, w);
            self.titleLabel.frame = CGRectMake(15+w+5, self.iconImage.frame.origin.y, APP_VIEW_WIDTH/2-w-20, w/2);
            self.contentLabel.frame = CGRectMake(self.titleLabel.frame.origin.x, self.titleLabel.frame.origin.y+self.titleLabel.frame.size.height,self.titleLabel.frame.size.width , w/2);
        }
            break;
        case 1:  //上
        {
            float w = self.frame.size.height/2-10;

            self.iconImage.frame =CGRectMake(15,8 , w, w);
            self.titleLabel.frame = CGRectMake(15, 8+w,self.frame.size.width-30, w/2);
            self.contentLabel.frame = CGRectMake(self.titleLabel.frame.origin.x, self.titleLabel.frame.origin.y+self.titleLabel.frame.size.height,self.titleLabel.frame.size.width , w/2);
        }
            break;
        case 2:  //右
        {
            self.titleLabel.frame = CGRectMake(8, self.frame.size.height/2-w/2, APP_VIEW_WIDTH/2-w-5-8, w/2);
            self.iconImage.frame =CGRectMake(self.titleLabel.frame.origin.x+self.titleLabel.frame.size.width,self.titleLabel.frame.origin.y , w, w);
            self.contentLabel.frame = CGRectMake(self.titleLabel.frame.origin.x, self.titleLabel.frame.origin.y+self.titleLabel.frame.size.height,self.titleLabel.frame.size.width , w/2);

        }
            break;
        case 3:   //下
        {
            float w = self.frame.size.height/2-10;

            self.titleLabel.frame = CGRectMake(15, 8, self.frame.size.width-30, w/2);
            self.contentLabel.frame = CGRectMake(self.titleLabel.frame.origin.x, self.titleLabel.frame.origin.y+self.titleLabel.frame.size.height,self.titleLabel.frame.size.width , w/2);
            self.iconImage.frame =CGRectMake(15,8+w , w, w);


        }
            break;
        case 4: //中
        {
//            self.iconImage.frame = CGRectMake(0, 0, self.frame.size.height-15, self.frame.size.height-15);
            self.iconImage.frame = self.frame;
            self.iconImage.center = CGPointMake(self.frame.size.width/2, self.frame.size.height/2);
            self.titleLabel.frame = CGRectZero;
            self.contentLabel.frame = CGRectZero;
        }
            break;
            
        default:
            break;
    }
     

//}
 */
@end


@implementation BMSQ_homeBrandTableViewCell
-(id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {

    }
    return self;
}
/*
#pragma mark --
-(void)setHomeBrandCell:(NSArray *)reponsend height:(float)height{
    for (UIView *aboveView in self.subviews) {
        [aboveView removeFromSuperview];
    }
    UIView *bgView = [[UIView alloc]initWithFrame:CGRectMake(0, 10, APP_VIEW_WIDTH, height-10)];
    bgView.backgroundColor  = [UIColor clearColor];
    [self addSubview:bgView];
    float w = bgView.frame.size.width;
    float h = bgView.frame.size.height;
    NSArray *array = reponsend;
    if (array.count == 2 ) {
        w = (bgView.frame.size.width)/2;
    }else if (array.count>2){
        w = (bgView.frame.size.width)/2;
        int row =0;
        if (array.count%2 ==0) {
            row = (int)array.count /2;
        }else{
            row = (int)array.count /2+1;
            
        }
        h = ((bgView.frame.size.height)-(row-1))/row;
        
    }
    [self setBrandFrame:array w:w h:h view:bgView isActivity:NO];
}
 */
#pragma mark ----
-(void)setHomeActivityCell:(NSArray *)reponsend height:(float)height{
    for (UIView *aboveView in self.subviews) {
        [aboveView removeFromSuperview];
    }
    self.secH = 0;
    UIView *bgView = [[UIView alloc]initWithFrame:CGRectMake(0, 10, APP_VIEW_WIDTH, height-10)];
    bgView.backgroundColor  = [UIColor clearColor];
    [self addSubview:bgView];
    float w = bgView.frame.size.width;
    float h = bgView.frame.size.height;
    NSArray *array = reponsend;
    
    if (array.count == 2 ) {
        w = (bgView.frame.size.width)/2;
    }else if (array.count>2){
        w = (bgView.frame.size.width)/2;
        int row =0;
        if (array.count%2 ==0) {
            row = (int)array.count /2;
        }else{
            row = (int)array.count /2+1;
            
        }
        h = ((bgView.frame.size.height)-(row-1))/row;
        
    }
    
    if (array.count %2 ==0) {
        [self setBrandFrame:array w:w h:h view:bgView isActivity:YES];
    }else{
        if(array.count>=3){
            [self setBrandFrame:[array subarrayWithRange:NSMakeRange(0, array.count-3)] w:w h:h view:bgView isActivity:YES];
            NSArray *last3Array=[array subarrayWithRange:NSMakeRange(array.count-3, 3)];
            [self secFrame:last3Array bgView:bgView w:w h:h countArray:(int)array.count];
        }else{
            
            [self setBrandFrame:array w:w h:h view:bgView isActivity:YES];

        }
    }
}
-(void)setBrandFrame:(NSArray *)array w:(float)w h:(float)h view:(UIView *)bgView isActivity:(BOOL)b{

       for (int i = 0;  i < array.count; i++) {
        NSDictionary *dic = [array objectAtIndex:i];
        int row =0;
        if (i%2 ==0) {
            row = (int)i /2;
        }else{
            row = (int)i /2+1;
        }
        CGRect frame;
        if (i%2==0) {
          frame = CGRectMake(0, row*h, w-0.5, h-0.5);
        }else{
           frame = CGRectMake(w,(row-1)*h, w-0.5, h-0.5);
        }
        self.secH = frame.origin.y+h;
 
           NSString *imgUrl;
           if (self.currenType==100) {  //品牌 showImage
               if ([[dic objectForKey:@"showImg"]isKindOfClass:[NSNull class]]) {
                   imgUrl = @"";
               }else{
                   imgUrl = [dic objectForKey:@"showImg"];
               }
           }else{     //活动 功能 imageUrl
               if ([[dic objectForKey:@"imgUrl"]isKindOfClass:[NSNull class]]) {
                   imgUrl = @"";
               }else{
                   imgUrl = [dic objectForKey:@"imgUrl"];
               }
           }
           
         
        if (array.count==1) {
            HomeBrandView *brandView = [[HomeBrandView alloc]initWithFrame:CGRectMake(0, 0, APP_VIEW_WIDTH, h)];
            [brandView setIconImageStr:imgUrl];
            brandView.delegate = self;
            brandView.myDic = dic;
             [bgView addSubview:brandView];

        }else{
            HomeBrandView *brandView = [[HomeBrandView alloc]initWithFrame:frame];
            [brandView setIconImageStr:imgUrl];
            brandView.myDic = dic;
            brandView.delegate = self;
            [bgView addSubview:brandView];
        }

    }
 
  
}
#pragma mark ----最后三个
-(void)secFrame:(NSArray *)array bgView:(UIView *)bgView w:(float)w h:(float)h countArray:(int)countcountArray{
    
    UIView *view1 = [[UIView alloc]initWithFrame:CGRectMake(0,self.secH , w-0.5, h*2-0.5)];
    view1.backgroundColor = [UIColor redColor];
    [bgView addSubview:view1];
    UIView *view2 = [[UIView alloc]initWithFrame:CGRectMake(w,view1.frame.origin.y , w-0.5, h-0.5)];
    view2.backgroundColor = [UIColor whiteColor];
    [bgView addSubview:view2];
    UIView *view3 = [[UIView alloc]initWithFrame:CGRectMake(w,view2.frame.origin.y+h , w-0.5, h-0.5)];
    view3.backgroundColor = [UIColor blackColor];
    [bgView addSubview:view3];
    for (int i= 0;i<3; i++) {
        NSDictionary *dic = [array objectAtIndex:i];
  
        NSString *imgUrl;
        if (self.currenType==100) {  //品牌 showImage
            if ([[dic objectForKey:@"showImg"]isKindOfClass:[NSNull class]]) {
                imgUrl = @"";
            }else{
                imgUrl = [dic objectForKey:@"showImg"];
            }
        }else{     //活动 功能 imageUrl
            if ([[dic objectForKey:@"imgUrl"]isKindOfClass:[NSNull class]]) {
                imgUrl = @"";
            }else{
                imgUrl = [dic objectForKey:@"imgUrl"];
            }
        }
        
        if (i == 0) {
            HomeBrandView *brandView = [[HomeBrandView alloc]initWithFrame:view1.bounds];
            brandView.delegate = self;
            [view1 addSubview:brandView];
         
            brandView.myDic = dic;
            [brandView setIconImageStr:imgUrl];

        }else if(i == 1){
            HomeBrandView *brandView = [[HomeBrandView alloc]initWithFrame:view2.bounds];
            brandView.delegate = self;
            [view2 addSubview:brandView];
            brandView.myDic = dic;
            [brandView setIconImageStr:imgUrl];

        }else if(i == 2){
            HomeBrandView *brandView = [[HomeBrandView alloc]initWithFrame:view3.bounds];
            brandView.delegate = self;
            [view3 addSubview:brandView];
            [brandView setIconImageStr:imgUrl];
            brandView.myDic = dic;

        }
        
    }
}
-(void)clickImage:(UITapGestureRecognizer *)tap{
//    [self.brandDelegate ciclikBrand:(int)tap.view.tag];
}

-(void)clickActImage:(UITapGestureRecognizer *)tap{
//    [self.brandDelegate ciclikActivity:(int)tap.view.tag];
 }


-(void)HomeBrandViewDelegate:(NSDictionary *)dic{

    
    if ([self.brandDelegate respondsToSelector:@selector(clickImage:)]) {
        [self.brandDelegate clickImage:dic];
    }
    
    
}
@end
