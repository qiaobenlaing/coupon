//
//  CommitCell.m
//  BMSQC
//
//  Created by liuqin on 16/3/16.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "CommitCell.h"
#import "UIImageView+WebCache.h"
#import "CWStarRateView.h"

@interface CommitCell ()

@property (nonatomic, strong)UILabel *nameLabel;
@property (nonatomic, strong)CWStarRateView *starView;
@property (nonatomic, strong)UILabel *timeLabel;
@property (nonatomic, strong)UILabel *contentLabel;
@property (nonatomic, strong)UIScrollView *imageSCView;

@property (nonatomic, strong)UIView *replyView;


@property (nonatomic, strong)UILabel *shopreplyLabel;
@property (nonatomic, strong)UILabel *shopreplytimeLabel;
@property (nonatomic, strong)UILabel *shopreplycontentLabel;


@end


@implementation CommitCell

-(id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        self.nameLabel = [[UILabel alloc]init];
        self.nameLabel.textColor = APP_TEXTCOLOR;
        self.nameLabel.font = [UIFont systemFontOfSize:16.f];
        [self addSubview:self.nameLabel];
        
        self.starView = [[CWStarRateView alloc] initWithFrame:CGRectMake(10, 25, APP_VIEW_WIDTH/4, 15) numberOfStars:5 seleImage:@"iconfont-starred" norImage:@"iconfont-stargray"];
        self.starView.scorePercent = 0.0;
        self.starView.allowIncompleteStar = NO;
        self.starView.hasAnimation = YES;
        [self addSubview:self.starView];
        
        
        self.timeLabel = [[UILabel alloc]init];
        self.timeLabel.textColor = APP_TEXTCOLOR;
        self.timeLabel.font = [UIFont systemFontOfSize:12.f];
        self.timeLabel.textAlignment = NSTextAlignmentRight;
        [self addSubview:self.timeLabel];
        
        self.contentLabel = [[UILabel alloc]init];
        self.contentLabel.textColor = APP_TEXTCOLOR;
        self.contentLabel.font = [UIFont systemFontOfSize:13.f];
        self.contentLabel.numberOfLines = 0;
        [self addSubview:self.contentLabel];
        
        self.imageSCView = [[UIScrollView alloc]init];
        self.imageSCView.backgroundColor = [UIColor clearColor];
        [self addSubview:self.imageSCView];
        
        self.replyView = [[UIScrollView alloc]init];
        self.replyView.backgroundColor = UICOLOR(231, 231, 231, 1);
        [self addSubview:self.replyView];
        
        self.shopreplyLabel = [[UILabel alloc]init];
        self.shopreplyLabel.textColor = UICOLOR(114, 114, 114, 1);
        self.shopreplyLabel.font = [UIFont systemFontOfSize:12.f];
        self.shopreplyLabel.text = @"商家回复：";
        [self.replyView addSubview:self.shopreplyLabel];

        
        self.shopreplytimeLabel = [[UILabel alloc]init];
        self.shopreplytimeLabel.textColor = UICOLOR(177, 177, 177, 1);
        self.shopreplytimeLabel.font = [UIFont systemFontOfSize:12.f];
        self.shopreplytimeLabel.text = @"商家回复：";
        self.shopreplytimeLabel.textAlignment = NSTextAlignmentRight;
        [self.replyView addSubview:self.shopreplytimeLabel];
        
        self.shopreplycontentLabel = [[UILabel alloc]init];
        self.shopreplycontentLabel.textColor = UICOLOR(114, 114, 114, 1);
        self.shopreplycontentLabel.font = [UIFont systemFontOfSize:12.f];
        self.shopreplycontentLabel.numberOfLines = 0;
        [self.replyView addSubview:self.shopreplycontentLabel];
        
    }
    return self;
}

-(void)setCommitCell:(NSDictionary *)dic{
    
    
    for (id image in self.imageSCView.subviews) {
        if ([image isKindOfClass:[UIImageView class]]) {
            [image removeFromSuperview];
        }
    }
    
    
    
    self.nameLabel.frame = CGRectMake(10, 0, APP_VIEW_WIDTH-20, 25) ;
    self.nameLabel.text = [dic objectForKey:@"nickName"];
    
    int wholeLvl = [[NSString stringWithFormat:@"%@",[dic objectForKey:@"wholeLvl"]]intValue];
    self.starView.scorePercent = wholeLvl;
    
    self.timeLabel.frame = CGRectMake(APP_VIEW_WIDTH-100, 25, 90, 20) ;
    NSString *remarkTime = [dic objectForKey:@"remarkTime"];
    NSArray *array = [remarkTime componentsSeparatedByString:@" "];
    
    self.timeLabel.text =[array objectAtIndex:0] ;
    
    self.contentLabel.text =[dic objectForKey:@"remark"];
    CGSize size = [self.contentLabel.text boundingRectWithSize:CGSizeMake(APP_VIEW_WIDTH-20, MAXFLOAT)
                                                        options:NSStringDrawingUsesLineFragmentOrigin
                                                     attributes:@{NSFontAttributeName:self.contentLabel.font}
                                                        context:nil].size;
    self.contentLabel.frame = CGRectMake(10, 50, APP_VIEW_WIDTH-20, size.height+10);
    
   

    
    
    
    
    float w = ((APP_VIEW_WIDTH-20)-20)/3+20;
    float spx = 10;
    NSArray *classRemarkImg = [dic objectForKey:@"classRemarkImg"];
      self.imageSCView.contentSize = CGSizeMake(classRemarkImg.count*spx+classRemarkImg.count*w, self.imageSCView.frame.size.height);
    if (classRemarkImg.count>0) {
         self.imageSCView.frame = CGRectMake(10, self.contentLabel.frame.origin.y+self.contentLabel.frame.size.height, APP_VIEW_WIDTH-20, 100);
        
        for (int i=0; i<classRemarkImg.count; i++) {
            NSDictionary *dic = [classRemarkImg objectAtIndex:i];
            UIImageView *imageView =[[UIImageView alloc]initWithFrame:CGRectMake(i*spx+i*w, 0, w, self.imageSCView.frame.size.height)];
            
            [imageView sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@",IMAGE_URL,[dic objectForKey:@"remarkImgUrl"]]] placeholderImage:[UIImage imageNamed:@"iv_noDataHome"]];
            
            [self.imageSCView addSubview:imageView];
        }
        
      

    }else{
        self.imageSCView.frame = CGRectMake(10, self.contentLabel.frame.origin.y+self.contentLabel.frame.size.height, APP_VIEW_WIDTH-20, 0);
    }
    
   

   
    if ([[dic objectForKey:@"shopRemark"] isKindOfClass:[NSString class]]  ) {
        NSString *shopRemark =[dic objectForKey:@"shopRemark"];
        if(shopRemark.length>0){
            self.shopreplycontentLabel.text = [dic objectForKey:@"shopRemark"];
            size = [self.shopreplycontentLabel.text boundingRectWithSize:CGSizeMake(APP_VIEW_WIDTH-20, MAXFLOAT)
                                                                 options:NSStringDrawingUsesLineFragmentOrigin
                                                              attributes:@{NSFontAttributeName:self.shopreplycontentLabel.font}
                                                                 context:nil].size;
            self.shopreplycontentLabel.frame = CGRectMake(10, 20, self.replyView.frame.size.width-20, size.height+10+20);
            self.replyView.hidden = NO;
            self.replyView.frame = CGRectMake(10, self.imageSCView.frame.origin.y+self.imageSCView.frame.size.height+10, APP_VIEW_WIDTH-20, 20+self.shopreplycontentLabel.frame.size.height);
            
            self.shopreplytimeLabel.frame = CGRectMake(self.replyView.frame.size.width-100, 0, 90, 20) ;
            if ([[dic objectForKey:@"shopRemarkTime"] isKindOfClass:[NSString class]]) {
                self.shopreplytimeLabel.text =[dic objectForKey:@"shopRemarkTime"];
            }
            
            self.shopreplyLabel.frame = CGRectMake(10, 0, self.replyView.frame.size.width-20, 20) ;
        }else{
            self.replyView.hidden = YES;
 
        }
            
        


    }else{
        self.replyView.hidden = YES;
    }
  
    
    
}

+(float)commitHeigh:(NSDictionary *)dic{
    
    float h = 50;
    
    NSString *str = [dic objectForKey:@"remark"];
    CGSize size = [str boundingRectWithSize:CGSizeMake(APP_VIEW_WIDTH-20, MAXFLOAT)
                                                       options:NSStringDrawingUsesLineFragmentOrigin
                                                    attributes:@{NSFontAttributeName:[UIFont systemFontOfSize:13.f]}
                                                       context:nil].size;
    

    NSArray *classRemarkImg = [dic objectForKey:@"classRemarkImg"];
    if (classRemarkImg.count>0) {
        h=h+size.height+10+100+10;  //100图片

    }else{
        h=h+size.height+10;
    }
    

    if ([[dic objectForKey:@"shopRemark"] isKindOfClass:[NSString class]]) {
        str =[dic objectForKey:@"shopRemark"];
        if(str.length>0){
            size = [str boundingRectWithSize:CGSizeMake(APP_VIEW_WIDTH-20, MAXFLOAT)
                                     options:NSStringDrawingUsesLineFragmentOrigin
                                  attributes:@{NSFontAttributeName:[UIFont systemFontOfSize:12.f]}
                                     context:nil].size;
            h = h+size.height+30;
            
            
        }
    }
    
    return h;
    
}
@end
